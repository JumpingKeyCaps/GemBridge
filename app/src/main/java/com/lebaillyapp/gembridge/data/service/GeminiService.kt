package com.lebaillyapp.gembridge.data.service

/**
 * Contrat définissant les capacités de notre service d'IA.
 * * Dans ce projet, ce "Service" est une interface car elle sera implémentée
 * par trois stratégies différentes : Retrofit, le SDK officiel, et Firebase.
 */
interface GeminiService {

    /**
     * Envoie le prompt à l'IA et récupère la réponse brute.
     * @param prompt Le texte saisi par l'utilisateur.
     * @return [Result] encapsulant la String de réponse ou une erreur.
     */
    suspend fun generateResponse(prompt: String): Result<String>
}