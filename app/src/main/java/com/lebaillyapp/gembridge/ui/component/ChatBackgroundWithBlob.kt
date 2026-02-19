package com.lebaillyapp.gembridge.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lebaillyapp.gembridge.R
import com.lebaillyapp.gembridge.domain.model.ChatState

@Composable
fun ChatBackgroundWithBlob(
    modifier: Modifier = Modifier,
    chatState: ChatState,
    onBgClick: () -> Unit = {}
) {
    // On garde une trace du temps qui s'écoule de façon continue
    var time by remember { mutableStateOf(0f) }

    // On lance l'horloge UNE SEULE FOIS au démarrage (LaunchedEffect(Unit))
    // Le shader tournera toujours en fond, ce qui est très peu coûteux avec nos optimisations
    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (true) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    Box(modifier = modifier
        .fillMaxSize()
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null // On enlève l'effet "gris" du clic Android
        ) { onBgClick() }
    ) {
        // Le fond qui change de couleur selon l'état
        ShaderBackground(
            shaderRes = R.raw.bg_gradient_shader,
            chatState = chatState,
            modifier = Modifier.fillMaxSize(),
            time = time
        )

        // Le blob qui pulse et ondule au centre
        // Il réagit au 'time' continu et au 'chatState' pour son agitation



        ShaderBlob(
            shaderRes = R.raw.blob_shader,
            chatState = chatState,
            time = time,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )



    }
}