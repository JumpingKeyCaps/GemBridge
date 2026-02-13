package com.lebaillyapp.gembridge.domain.model

/**
 * ## ChatState
 * Représente l'état de la conversation.
 *
 * Encapsule les données de session, les indicateurs de chargement
 * et les éventuels messages d'erreur métier.
 */
data class ChatState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSettingsOpen: Boolean = false
)