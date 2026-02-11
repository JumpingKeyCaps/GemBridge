package com.lebaillyapp.gembridge.domain.usecase

import com.lebaillyapp.gembridge.data.repository.GeminiRepository
import javax.inject.Inject

/**
 * Use Case chargé de traiter la logique métier d'une réponse de chat.
 * C'est ici que résidera notre "boîte noire" de traitement (chaînage d'appels,
 * formatage JSON, etc.).
 */
class GetChatResponseUseCase @Inject constructor(
    private val repository: GeminiRepository
) {
    /**
     * Exécute la logique de génération de réponse.
     * Pour l'instant, c'est un simple relai, mais la structure est prête.
     */
    suspend operator fun invoke(userPrompt: String): Result<String> {
        // C'est ici qu'on pourra faire :
        // val intention = repository.getAiResponse("Analyse : $userPrompt")
        // val final = repository.getAiResponse("Réponds à $intention")

        return repository.getAiResponse(userPrompt)
    }
}