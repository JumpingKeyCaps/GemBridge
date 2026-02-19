package com.lebaillyapp.gembridge.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lebaillyapp.gembridge.ui.component.AiResponseDisplay

@Composable
fun AiChatScreen() {
    var aiResponse by remember { mutableStateOf("") }
    // Clé pour forcer la recomposition
    var refreshKey by remember { mutableIntStateOf(0) }

    val sampleText = "I am a generative AI model. My thoughts are emerging from the digital void, " +
            "sculpted by logic and shaped by your curiosity. Let's build something unique."

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // En utilisant key(), on force Compose à détruire et recréer le composant
        // quand refreshKey change, ce qui relance tous les LaunchedEffect.
        key(refreshKey) {
            if (aiResponse.isNotEmpty()) {
                AiResponseDisplay(
                    fullText = aiResponse
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                aiResponse = sampleText
                refreshKey++ // On incrémente pour reset l'animation
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Simulate AI Response", color = Color.White)
        }
    }
}