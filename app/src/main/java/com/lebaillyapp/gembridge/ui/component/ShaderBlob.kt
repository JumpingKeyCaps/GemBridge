package com.lebaillyapp.gembridge.ui.component


import android.graphics.RuntimeShader
import androidx.annotation.RawRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
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
fun ShaderBlob(
    @RawRes shaderRes: Int,
    chatState: ChatState,
    time: Float,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val shader = remember(shaderRes) {
        val source = context.resources
            .openRawResource(shaderRes)
            .bufferedReader()
            .use { it.readText() }
        RuntimeShader(source)
    }

    // On anime l'amplitude pour que le passage "Repos -> Loading" soit smooth
    val targetAmplitude = when {
        chatState.error != null -> 0.8f
        chatState.isLoading -> 0.5f
        else -> 0.2f
    }

    val animatedAmplitude by animateFloatAsState(
        targetValue = targetAmplitude,
        animationSpec = tween(durationMillis = 2500),
        label = "blobAmplitude"
    )

    Box(
        modifier = modifier
            .onSizeChanged { size ->
                shader.setFloatUniform("iResolution", size.width.toFloat(), size.height.toFloat())
            }
            .drawWithCache {
                shader.setFloatUniform("uTime", time)
                shader.setFloatUniform("uPulseAmplitude", animatedAmplitude)

                val brush = ShaderBrush(shader)
                onDrawBehind {
                    // On dessine un cercle ou un rect, le shader s'occupe de la forme
                    drawRect(brush)
                }
            }
    )
}