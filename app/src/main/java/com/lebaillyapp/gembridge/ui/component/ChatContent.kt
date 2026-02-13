package com.lebaillyapp.gembridge.ui.component

import com.lebaillyapp.gembridge.domain.model.ChatState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * # ChatContent
 *
 * The primary UI container for the chat interface. It manages the display of the message list,
 * the loading state, and the interaction bar.
 *
 * This component is designed to be decoupled from the sliding animation logic,
 * acting as a standard chat screen that can be translated or offset by a parent container.
 *
 * @param uiState The current state of the chat (messages, loading status, settings visibility).
 * @param onSendMessage Lambda triggered when the user sends a message.
 * @param onToggleSettings Lambda triggered to open or close the configuration panel.
 * @param modifier Modifier to be applied to the Scaffold.
 */
@Composable
fun ChatContent(
    uiState: ChatState,
    onSendMessage: (String) -> Unit,
    onToggleSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFF0B0D10), // On force le fond sombre
        bottomBar = {
            ChatInputBar(
                onSendMessage = { text ->
                    onSendMessage(text)
                    coroutineScope.launch {
                        if (uiState.messages.isNotEmpty()) {
                            listState.animateScrollToItem(uiState.messages.size)
                        }
                    }
                },
                onToggleSettings = onToggleSettings,
                isSettingsOpen = uiState.isSettingsOpen,
                modifier = Modifier.navigationBarsPadding()
            )
        }
    ) { paddingValues ->
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
                items(uiState.messages) { message ->
                    MessageBubble(message = message)
                }

                if (uiState.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color(0xffedff61)
                            )
                        }
                    }
                }
            }
        }
    }

    // Auto-scroll logic
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }
}