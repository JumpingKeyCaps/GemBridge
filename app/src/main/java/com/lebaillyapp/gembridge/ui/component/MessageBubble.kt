package com.lebaillyapp.gembridge.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lebaillyapp.gembridge.domain.model.ChatMessage

/**
 * # MessageBubble
 *  Composant d'interface représentant une bulle de texte unique dans la conversation.
 *  ### Logique d'affichage :
 * - **Utilisateur :** Aligné à droite (End), couleur primaire de l'application, coin inférieur droit "pointu".
 * - **IA (Gemini) :** Aligné à gauche (Start), couleur de conteneur secondaire, coin inférieur gauche "pointu".
 *  @param message L'objet [ChatMessage] contenant le contenu textuel et la source (user ou bot).
 */
@Composable
fun MessageBubble(message: ChatMessage) {
    // Analyse de la source pour déterminer le style
    val isUser = message.isFromUser

    // Configuration dynamique du layout
    val alignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart

    // Extraction des couleurs du thème Material 3
    val bubbleColor = if (isUser) {
        Color(0xFF14171E)
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }

    val textColor = if (isUser) {
        MaterialTheme.colorScheme.onSecondaryContainer
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer
    }

    // Container principal occupant toute la largeur pour permettre l'alignement
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 28.dp),
        contentAlignment = alignment
    ) {
        Surface(
            color = bubbleColor,
            // Construction asymétrique des coins pour l'effet "bulle de BD"
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 0.dp,
                bottomEnd = if (isUser) 0.dp else 16.dp
            ),
            tonalElevation = 2.dp // Légère ombre pour détacher la bulle du fond
        ) {
            Text(
                text = message.content,
                color = textColor,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}