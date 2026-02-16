package com.lebaillyapp.gembridge.data.service.mock

import com.lebaillyapp.gembridge.data.service.GeminiService
import kotlinx.coroutines.delay

/**
 * # MockGeminiService
 *
 * A simulated implementation of [GeminiService] used for architectural testing and UI prototyping.
 *
 * This class allows developers to verify the application flow (Loading -> Success/Error)
 * without consuming API quotas or requiring an active internet connection. It ensures that
 * the MVVM pattern and Dependency Injection are correctly wired.
 */
class MockGeminiService : GeminiService {

    /**
     * ### GenerateResponse
     * Simulates an AI response after a synthetic network delay.
     *
     * @param prompt The input text from the user.
     * @return A [Result.success] containing a formatted string that echoes the input,
     * confirming the data loop is functional.
     */
    override suspend fun generateResponse(prompt: String): Result<String> {
        // Artificial delay (1.5 seconds) to simulate network latency and test UI loading states.
        delay(1500)

        return Result.success(
            "This is a simulated (Mock) response from GemBridge. " +
                    "I have successfully received your message: \"$prompt\""
        )
    }
}