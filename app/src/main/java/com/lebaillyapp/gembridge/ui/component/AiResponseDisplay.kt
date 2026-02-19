package com.lebaillyapp.gembridge.ui.component

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.lebaillyapp.gembridge.R

@Composable
fun AiResponseDisplay(
    fullText: String,
    fontSize: TextUnit = 16.sp,
    charDelay: Long = 45L
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val shaderSource = remember {
        context.resources.openRawResource(R.raw.text_reveal_optical).use { it.bufferedReader().readText() }
    }
    val runtimeShader = remember(shaderSource) { RuntimeShader(shaderSource) }

    var lastCharPos by remember { mutableStateOf(Offset.Zero) }
    var displayedCharCount by remember { mutableIntStateOf(0) }
    var isWriting by remember { mutableStateOf(true) }

    val laserIntensity by animateFloatAsState(
        targetValue = if (isWriting) 1f else 0f,
        animationSpec = tween(1000, easing = LinearOutSlowInEasing),
        label = "LaserIntensity"
    )

    val time by rememberInfiniteTransition().animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(100000, easing = LinearEasing))
    )

    LaunchedEffect(fullText) {
        isWriting = true
        displayedCharCount = 0
        for (i in 1..fullText.length) {
            delay(charDelay)
            displayedCharCount = i
        }
        delay(150)
        isWriting = false
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .graphicsLayer {
                runtimeShader.setFloatUniform("cursor", lastCharPos.x, lastCharPos.y)
                runtimeShader.setFloatUniform("radius", fontSize.toPx() * 1.2f)
                runtimeShader.setFloatUniform("time", time)
                runtimeShader.setFloatUniform("laserIntensity", laserIntensity)

                renderEffect = RenderEffect.createRuntimeShaderEffect(
                    runtimeShader, "composable"
                ).asComposeRenderEffect()
            }
    ) {
        Text(
            text = fullText.take(displayedCharCount),
            style = LocalTextStyle.current.copy(
                fontSize = fontSize,
                lineHeight = fontSize * 1.4f,
                letterSpacing = 1.sp
            ),
            color = Color.White,
            onTextLayout = { textLayoutResult ->
                if (displayedCharCount > 0) {
                    val lastIndex = (displayedCharCount - 1).coerceIn(0, fullText.length - 1)
                    val rect = textLayoutResult.getCursorRect(lastIndex)
                    if (rect.center.x > 0) lastCharPos = rect.center
                }
            }
        )
    }
}