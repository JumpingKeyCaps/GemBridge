package com.lebaillyapp.gembridge.data.service

import kotlinx.coroutines.delay

/**
 * Version simulée (Mock) du service Gemini.
 * * Cet objet permet de tester l'architecture et l'interface utilisateur
 * sans effectuer d'appels réseau réels. Il simule un délai de traitement
 * pour valider visuellement les états de chargement dans l'UI.
 */
class MockGeminiService : GeminiService {

    /**
     * Simule une réponse de l'IA après un court délai.
     * @param prompt Le texte envoyé par l'utilisateur.
     * @return Un [Result] contenant une réponse pré-enregistrée incluant le prompt original.
     */
    override suspend fun generateResponse(prompt: String): Result<String> {
        // Simule la latence réseau (1,5 seconde) pour tester l'UI
        delay(1500)

        return Result.success(
            "Ceci est une réponse simulée (Mock) de GemBridge. " +
                    "J'ai bien reçu ton message : \"$prompt\""
        )
    }
}