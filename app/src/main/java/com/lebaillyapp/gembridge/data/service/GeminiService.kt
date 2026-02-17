package com.lebaillyapp.gembridge.data.service

import com.lebaillyapp.gembridge.domain.model.GeminiConfig

/**
 * # GeminiService
 *
 * Core contract defining the AI generation capabilities of the application.
 *
 * This interface acts as an abstraction layer to decouple the business logic from specific
 * implementation details. In the GemBridge PoC, this allows seamless switching between:
 * 0. **Mock** (for testing)
 * 1. **Retrofit** (Direct REST API)
 * 2. **Google AI SDK** (Official client library)
 * 3. **Firebase Vertex AI** (Managed production-grade integration)
 */
interface GeminiService {

    /**
     * ### GenerateResponse
     * Sends a raw prompt to the AI model and retrieves the generated response.
     *
     * @param prompt The user's input.
     * @param config The specific configuration for this request.
     * @return A [Result] wrapping the successful [String] response or a [Throwable] describing the failure.
     *
     * @see <a href="https://ai.google.dev/gemini-api/docs">Gemini API Documentation</a>
     */
    suspend fun generateResponse(prompt: String, config: GeminiConfig): Result<String>
}