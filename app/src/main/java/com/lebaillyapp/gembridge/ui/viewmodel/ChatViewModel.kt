package com.lebaillyapp.gembridge.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lebaillyapp.gembridge.domain.usecase.GetChatResponseUseCase
import com.lebaillyapp.gembridge.domain.model.ChatMessage
import com.lebaillyapp.gembridge.domain.model.ChatState
import com.lebaillyapp.gembridge.domain.model.GeminiConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * # ChatViewModel
 * ViewModel pilotant l'écran de discussion.
 *
 * Il transforme les intentions de l'utilisateur (envoyer un message) en état (State)
 * en communiquant avec le usecase.
 */
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatResponse: GetChatResponseUseCase
) : ViewModel() {

    // L'état privé, modifiable uniquement dans cette classe
    private val _uiState = MutableStateFlow(ChatState())

    // L'état public, exposé en lecture seule pour la vue Compose
    val uiState: StateFlow<ChatState> = _uiState.asStateFlow()

    /**
     * ## onSendMessage
     * Traite l'envoi d'un message utilisateur.
     * @param content Le texte saisi par l'utilisateur.
     */
    fun onSendMessage(content: String, config: GeminiConfig) {
        //0 - Sécurité : on n'envoie pas de messages vides
        if (content.isBlank()) return

        //1 - Création de l'objet message utilisateur
        val userMessage = ChatMessage(
            content = content,
            isFromUser = true
        )

        //2 - On met à jour l'UI : on ajoute le message et on déclenche le loader
        _uiState.update { currentState ->
            currentState.copy(
                messages = currentState.messages + userMessage,
                isLoading = true,
                error = null // On reset l'erreur s'il y en avait une
            )
        }

        //3 - Lancement de l'appel asynchrone
        viewModelScope.launch {
            // Appel au repository (qui est déjà Main-safe grâce au Dispatchers.IO )
            val result = getChatResponse(content,config)

            result.fold(
                onSuccess = { responseText ->
                    // Succès : on ajoute la réponse de l'IA et on coupe le loader
                    val aiMessage = ChatMessage(
                        content = responseText,
                        isFromUser = false
                    )
                    _uiState.update { it.copy(
                        messages = it.messages + aiMessage,
                        isLoading = false
                    ) }
                },
                onFailure = { error ->
                    // Échec : on affiche l'erreur et on coupe le loader
                    _uiState.update { it.copy(
                        error = error.localizedMessage ?: "Erreur réseau",
                        isLoading = false
                    ) }
                }
            )
        }
    }

    /**
     * ## toggleSettings
     * Met a jour l'etat de la conversation quand le pannel setting est ouvert/fermer.
     */
    fun toggleSettings() {
        _uiState.update { it.copy(isSettingsOpen = !it.isSettingsOpen) }
    }

}