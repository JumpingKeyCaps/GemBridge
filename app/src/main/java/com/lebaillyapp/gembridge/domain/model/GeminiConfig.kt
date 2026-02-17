package com.lebaillyapp.gembridge.domain.model

/**
 * # GeminiConfig
 *
 * Data holder for AI model parameters.
 *
 * @property temperature Controls randomness: 0.0 is deterministic, 1.0 is highly creative.
 * @property topP Nucleus sampling: higher values allow for more diverse vocabulary.
 * @property topK Limits the cumulative probability of next-word tokens.
 * @property systemPrompt Defines the AI's persona and operational constraints.
 */
data class GeminiConfig(
    val temperature: Float = 0.7f,
    val topP: Float = 0.95f,
    val topK: Int = 40,
    val systemPrompt: String = "You are a helpful AI assistant."
)