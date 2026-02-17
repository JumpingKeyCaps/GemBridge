package com.lebaillyapp.gembridge.data.service.mock

import com.lebaillyapp.gembridge.data.service.GeminiService
import com.lebaillyapp.gembridge.domain.model.GeminiConfig
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * # MockGeminiService
 *
 * A simulated implementation of [GeminiService] used for architectural testing and UI prototyping.
 *
 * This class adheres to the dynamic configuration contract, allowing UI components to pass
 * [GeminiConfig] without triggering real API calls. It is essential for verifying state
 * management (Loading/Success/Error) in a sandbox environment.
 */
class MockGeminiService @Inject constructor() : GeminiService {

    /**
     * Simulates an AI response after a synthetic network delay.
     *
     * @param prompt The input text from the user.
     * @param config The [GeminiConfig] containing generation parameters (simulated here).
     * @return A [Result.success] echoing the prompt and reflecting the provided configuration.
     */
    override suspend fun generateResponse(prompt: String, config: GeminiConfig): Result<String> {
        // Artificial delay (1.5 seconds) to simulate network latency and test UI loading states.
        delay(1500)

        return Result.success(
            "This is a simulated (Mock) response from GemBridge.\n\n" +
                    "Configuration used:\n" +
                    "- Temperature: ${config.temperature}\n" +
                    "- System Prompt: \"${config.systemPrompt}\"\n\n" +
                    "I have successfully received your message: \"$prompt\""
        )
    }
}