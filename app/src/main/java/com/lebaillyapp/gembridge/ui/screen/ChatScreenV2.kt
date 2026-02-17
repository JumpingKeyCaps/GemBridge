package com.lebaillyapp.gembridge.ui.screen


import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.lebaillyapp.gembridge.domain.model.GeminiConfig
import com.lebaillyapp.gembridge.ui.component.ChatContent
import com.lebaillyapp.gembridge.ui.component.SettingsPanel
import com.lebaillyapp.gembridge.ui.viewmodel.ChatViewModel
/**
 * # ChatScreenV2
 *
 * The main screen of the GemBridge application. It implements a sophisticated "sliding layout"
 * where the chat interface is layered on top of the settings panel.
 *
 * ### UI Architecture:
 * - **Background Layer:** The [SettingsPanel] containing AI parameters.
 * - **Foreground Layer:** The [ChatContent] containing the message list and input bar.
 *
 * When the settings are toggled, the foreground layer translates vertically upwards
 * to reveal the configuration controls underneath.
 *
 * @param viewModel The [ChatViewModel] that drives the UI state and handles business logic.
 */
@Composable
fun ChatScreenV2(
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val settingsPanelHeight = 630.dp // Hauteur du panneau de réglages

    // Animation de la translation verticale
    val translateY by animateDpAsState(
        targetValue = if (uiState.isSettingsOpen) -settingsPanelHeight else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow), // Effet ressort fluide
        label = "ChatTranslation"
    )

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0B0D10))) {

        // 1. LE PANNEAU DE RÉGLAGES (Derrière)
        SettingsPanel(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(settingsPanelHeight)
        )

        // 2. LE CONTENU DU CHAT (Devant)
        // On applique l'offset ici : quand translateY change, tout le bloc monte
        Box(modifier = Modifier
            .fillMaxSize()
            .offset(y = translateY)
        ) {
            // Ton Scaffold actuel (avec la modif de la ChatInputBar)
            ChatContent(
                uiState = uiState,
                onSendMessage = { viewModel.onSendMessage(it, GeminiConfig()) },// todo- plug the real config to use
                onToggleSettings = { viewModel.toggleSettings() }
            )
        }
    }
}