package com.lebaillyapp.gembridge.ui.component

import android.graphics.RuntimeShader
import androidx.annotation.RawRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import com.lebaillyapp.gembridge.domain.model.ChatState

@Composable
fun ShaderBackground(
    @RawRes shaderRes: Int,
    chatState: ChatState,
    time: Float,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // 1. Chargement unique du shader
    val shader = remember(shaderRes) {
        val source = context.resources
            .openRawResource(shaderRes)
            .bufferedReader()
            .use { it.readText() }
        RuntimeShader(source)
    }

    // 2. Animation fluide du uProgress (évite les flashs de couleur)
    val targetProgress = when {
        chatState.error != null -> 1f
        chatState.isLoading -> 0.5f
        else -> 0f
    }

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 2000), // Transition douce de 2000ms
        label = "backgroundProgress"
    )

    // 3. Dessin optimisé
    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { size ->
                // Mise à jour de la résolution uniquement quand la taille change
                shader.setFloatUniform(
                    "iResolution",
                    size.width.toFloat(),
                    size.height.toFloat()
                )
            }
            .drawWithCache {
                // On injecte le progrès animé juste avant le dessin
                shader.setFloatUniform("uTime", time)
                shader.setFloatUniform("uProgress", animatedProgress)

                val brush = ShaderBrush(shader)
                onDrawBehind {
                    drawRect(brush)
                }
            }
    )
}
