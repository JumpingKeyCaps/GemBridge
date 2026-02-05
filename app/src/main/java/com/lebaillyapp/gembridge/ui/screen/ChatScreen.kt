package com.lebaillyapp.gembridge.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lebaillyapp.gembridge.ui.component.ChatInputBar
import com.lebaillyapp.gembridge.ui.component.MessageBubble
import com.lebaillyapp.gembridge.ui.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

/**
 * # ChatScreen
 * Écran principal de la discussion.
 * Il fait le lien entre la logique métier (ViewModel) et les composants UI.
 * Gère l'affichage de la liste des messages, l'état de chargement et la saisie utilisateur.
 * @param viewModel Injecté automatiquement par Hilt via [hiltViewModel].
 */
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel()
) {
    // Collecte de l'état UI en provenance du StateFlow du ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Gestion de l'état de la liste (nécessaire pour le contrôle du scroll)
    val listState = rememberLazyListState()

    // Portée de coroutine pour déclencher des actions asynchrones sur l'UI (comme le scroll)
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        // On place la barre de saisie dans le slot bottomBar pour qu'elle reste fixe en bas
        bottomBar = {
            ChatInputBar(
                onSendMessage = { text ->
                    viewModel.onSendMessage(text)
                    // Scroll immédiat vers le bas lors d'un envoi utilisateur
                    coroutineScope.launch {
                        if (uiState.messages.isNotEmpty()) {
                            listState.animateScrollToItem(uiState.messages.size)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        // Le contenu principal s'adapte aux padding du Scaffold (évite les chevauchements)
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
            ) {
                // Rendu de chaque message de la liste
                items(uiState.messages) { message ->
                    MessageBubble(message = message)
                }

                // Affichage d'un indicateur si une réponse est attendue
                if (uiState.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        }
                    }
                }
            }
        }
    }

    /**
     * AUTO-SCROLL LOGIC
     * Se déclenche à chaque fois que la taille de la liste de messages change.
     * Permet de garder le dernier message visible, notamment lors de la réponse de l'IA.
     */
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }
}