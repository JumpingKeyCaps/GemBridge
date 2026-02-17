package com.lebaillyapp.gembridge.data.repository

import com.lebaillyapp.gembridge.data.service.GeminiService
import com.lebaillyapp.gembridge.domain.model.GeminiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Point d'entrée unique pour la gestion des données liées à l'IA.
 * * Le Repository orchestre les appels aux services et s'assure que les opérations
 * sont effectuées sur le bon thread. Il permet de découpler la logique métier
 * des implémentations techniques des services.
 *
 * @property geminiService L'implémentation du service à utiliser (Mock, SDK, Retrofit, etc.).
 */
class GeminiRepository @Inject constructor(
    private val geminiService: GeminiService
) {

    /**
     * Récupère la réponse de l'IA pour un prompt donné.
     * * On utilise [withContext] avec [Dispatchers.IO] pour garantir que l'appel
     * réseau (ou sa simulation) ne bloque pas le thread principal (UI).
     *
     * @param prompt Le texte saisi par l'utilisateur.
     * @return Le [Result] contenant la réponse textuelle.
     */
    suspend fun getAiResponse(prompt: String, config: GeminiConfig): Result<String> {
        return withContext(Dispatchers.IO) {
            geminiService.generateResponse(prompt, config)
        }
    }
}