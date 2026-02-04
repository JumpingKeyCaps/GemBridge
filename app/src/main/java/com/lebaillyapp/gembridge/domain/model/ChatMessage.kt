package com.lebaillyapp.gembridge.domain.model

import java.util.UUID

/**
 * # ChatMessage
 * ### Représente un message unique au sein d'une conversation.
 * * Cette classe est agnostique vis-à-vis des SDK AI : elle sert de modèle de référence
 * pour l'interface utilisateur (Compose) et le domaine.
 *
 * @property id Identifiant unique du message (généré par défaut via UUID).
 * @property content Le texte brut du message (soit l'entrée utilisateur, soit la réponse AI).
 * @property isFromUser Définit si le message a été envoyé par l'utilisateur (true) ou par l'IA (false).
 * @property timestamp Heure de création en millisecondes, utile pour le tri et l'affichage chronologique.
 */
data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)