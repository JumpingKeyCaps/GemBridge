package com.lebaillyapp.gembridge.data.service.generativeAi

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.lebaillyapp.gembridge.BuildConfig
import com.lebaillyapp.gembridge.data.service.GeminiService
import com.lebaillyapp.gembridge.domain.model.GeminiConfig
import javax.inject.Inject

/**
 * **GoogleGeminiService**
 *
 * Concrete implementation of [GeminiService] leveraging the official **Google Generative AI SDK**.
 *
 * This implementation is stateless regarding the model's configuration: it reconstructs
 * the [GenerativeModel] for each request to allow dynamic parameter tuning (temperature,
 * system prompts, etc.) directly from the UI or UseCases.
 */
class GoogleGeminiService @Inject constructor(
    private val apiKey: String
) : GeminiService {

    // model version
    private val modelName = "gemini-1.5-flash"

    /**
     * Executes a content generation request with dynamic parameters.
     *
     * @param prompt The user's input string.
     * @param config The [GeminiConfig] containing temperature, topP, and system instructions.
     * @return A [Result] containing the generated text or the failure exception.
     */
    override suspend fun generateResponse(prompt: String, config: GeminiConfig): Result<String> {
        return try {
            // 1. Build a one-shot model with the dynamic configuration
            val dynamicModel = GenerativeModel(
                modelName = modelName,
                apiKey = apiKey,
                generationConfig = generationConfig {
                    temperature = config.temperature
                    topP = config.topP
                    topK = config.topK
                },
                systemInstruction = content { text(config.systemPrompt) }
            )

            // 2. Execute the request
            val response = dynamicModel.generateContent(prompt)

            // 3. Extract and validate text
            val responseText = response.text
                ?: throw Exception("Gemini failed to generate text (potential safety block or empty response).")

            Result.success(responseText)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}