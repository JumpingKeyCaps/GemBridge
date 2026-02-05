package com.lebaillyapp.gembridge.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * # ChatInputBar
 * Composant de saisie textuelle situé généralement en bas de l'écran de discussion.
 *  ### Fonctionnement :
 * - Gère son propre état local ([textState]) pour la saisie en cours.
 * - Le bouton d'envoi n'est activé que si le texte n'est pas vide ou composé uniquement d'espaces.
 * - Réinitialise le champ de saisie automatiquement après avoir déclenché l'action d'envoi.
 * @param onSendMessage Lambda déclenchée lors du clic sur le bouton d'envoi, transmettant le texte saisi.
 * @param modifier Modificateur optionnel pour l'agencement externe du composant.
 */
@Composable
fun ChatInputBar(
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var textState by remember { mutableStateOf("") }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = textState,
            onValueChange = { textState = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Pose-moi une question...") },
            shape = CircleShape,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        IconButton(
            onClick = {
                if (textState.isNotBlank()) {
                    onSendMessage(textState)
                    textState = "" // On vide le champ après envoi
                }
            },
            enabled = textState.isNotBlank()
        ) {
            Icon(Icons.Default.Send, contentDescription = "Envoyer")
        }
    }
}