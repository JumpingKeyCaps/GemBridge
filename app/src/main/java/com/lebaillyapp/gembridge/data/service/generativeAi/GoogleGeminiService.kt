package com.lebaillyapp.gembridge.data.service.generativeAi

import com.google.ai.client.generativeai.GenerativeModel
import com.lebaillyapp.gembridge.data.service.GeminiService
import javax.inject.Inject

/**
 * # GoogleGeminiService
 *
 * Concrete implementation of [GeminiService] leveraging the official **Google Generative AI SDK**.
 *
 * This service communicates directly with Gemini models using the client provided by Google.
 * It represents the second pillar of the GemBridge integration strategy, offering a
 * straightforward, client-side implementation for rapid development and multimodal support.
 *
 * @property generativeModel The [GenerativeModel] instance (e.g., Gemini 1.5 Flash) injected via Hilt.
 */
class GoogleGeminiService @Inject constructor(
    private val generativeModel: GenerativeModel
) : GeminiService {

    /**
     * ### GenerateResponse
     * Executes a content generation request using the official Google SDK.
     *
     * The process involves:
     * 1. Invoking the suspending [GenerativeModel.generateContent] method.
     * 2. Validating the response (handling potential null values from safety filters).
     * 3. Catching and encapsulating exceptions into a [Result] wrapper.
     *
     * @param prompt The user's input string.
     * @return A [Result] containing the generated text or an exception describing the failure
     * (e.g., network issues, invalid API key, or safety blockages).
     */
    override suspend fun generateResponse(prompt: String): Result<String> {
        return try {
            // SDK Call: generateContent is a native suspending function
            val response = generativeModel.generateContent(prompt)

            // Extract text. If null, throw an exception to be caught in the block below.
            // Note: response.text can be null if the model blocks the output due to safety settings.
            val responseText = response.text ?: throw Exception("Gemini n'a pas pu générer de texte.")

            Result.success(responseText)
        } catch (e: Exception) {
            // Encapsulates all errors (network, quota, invalid API Key, etc.)
            Result.failure(e)
        }
    }
}