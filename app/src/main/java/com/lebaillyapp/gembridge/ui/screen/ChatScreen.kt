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

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel() // Hilt injecte le VM ici
) {
    // On observe l'état du ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Pour gérer le scroll automatique
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // Notre barre de saisie (composant créé juste avant)
            ChatInputBar(
                onSendMessage = { text ->
                    viewModel.onSendMessage(text)
                    // Scroll vers le bas quand l'utilisateur envoie
                    coroutineScope.launch {
                        listState.animateScrollToItem(uiState.messages.size)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // La liste des messages
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(uiState.messages) { message ->
                    MessageBubble(message = message)
                }

                // Petit indicateur de chargement si l'IA réfléchit
                if (uiState.isLoading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(16.dp)
                                .size(24.dp)
                        )
                    }
                }
            }
        }
    }

    // Scroll automatique quand l'IA répond
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }
}