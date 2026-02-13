package com.lebaillyapp.gembridge.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lebaillyapp.gembridge.R

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
    onToggleSettings: () -> Unit,
    isSettingsOpen: Boolean,
    modifier: Modifier = Modifier
) {
    var textState by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 18.dp, end = 18.dp, bottom = 10.dp, top = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // --- BOUTON RÉGLAGES ---
        IconButton(
            onClick = onToggleSettings,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = if (isSettingsOpen) Icons.Default.Close else Icons.Default.Settings,
                contentDescription = "Settings",
                tint = if (isSettingsOpen) Color(0xffedff61) else Color(0xFF606065)
            )
        }


        TextField(
            value = textState,
            onValueChange = { textState = it },
            modifier = Modifier.weight(1f).padding(start = 5.dp),
            placeholder = { Text("Ask me anything...",Modifier.padding(start = 18.dp)) },
            shape = RoundedCornerShape(26.dp),
            colors = TextFieldDefaults.colors(
                // La couleur quand on tape dedans
                focusedContainerColor = Color(0xFF16191F),
                // La couleur quand il est au repos
                unfocusedContainerColor = Color(0xFF12141A),
                // Tes paramètres actuels pour cacher la ligne du bas
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                // Optionnel : la couleur du curseur
                cursorColor = Color(0xffedff61)
            ),
            leadingIcon = {
                Icon(
                    modifier = Modifier.padding( start = 8.dp),
                        //.graphicsLayer(rotationY = 180f),
                    painter = painterResource(id = R.drawable.scool_ico_micro),
                    tint = Color.Unspecified,
                    contentDescription = "ic_app")
            },
            trailingIcon = {
                IconButton(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = {
                        if (textState.isNotBlank()) {
                            onSendMessage(textState)
                            textState = ""
                            keyboardController?.hide() //  Fermeture du clavier
                        }
                    },
                    enabled = textState.isNotBlank()
                ) {
                    Icon(
                        modifier = Modifier.padding( 8.dp),
                        painter = painterResource(id = R.drawable.ic_send),
                        tint = Color(0xFF606065),
                        contentDescription = "Envoyer")
                }
            }


        )


    }
}