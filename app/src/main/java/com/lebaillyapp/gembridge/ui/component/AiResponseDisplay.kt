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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.lebaillyapp.gembridge.R



import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

@Composable
fun AiResponseDisplay(
    fullText: String,
    fontSize: TextUnit = 16.sp,
    charDelay: Long = 40L,
    onCharAdded: () -> Unit = {}
) {
    val context = LocalContext.current
    val shaderSource = remember {
        context.resources.openRawResource(R.raw.text_reveal_optical).use { it.bufferedReader().readText() }
    }
    val runtimeShader = remember(shaderSource) { RuntimeShader(shaderSource) }

    var lastCharPos by remember { mutableStateOf(Offset.Zero) }
    var displayedCharCount by remember { mutableIntStateOf(0) }
    var isWriting by remember { mutableStateOf(true) }

    // --- 1. CONFIGURATION DES COULEURS ---
    val highlights = remember {
        mapOf(
            "[  OK  ]" to Color(0xFF00FF88),   // Vert émeraude
            "[ WARN ]" to Color(0xFFFFBB00),  // Ambre
            "root@gembridge" to Color(0xFF00CCFF), // Bleu cyan
            "ERROR" to Color(0xFFFF3377)      // Rose/Rouge laser
        )
    }

    // --- 2. LOGIQUE DE COLORIAGE ---
    // Cette fonction transforme le texte brut "coupé" en texte coloré
    val annotatedText = remember(displayedCharCount, fullText) {
        val currentRawText = fullText.take(displayedCharCount)
        buildAnnotatedString {
            append(currentRawText)
            // On scanne le texte actuel pour appliquer les styles
            highlights.forEach { (key, color) ->
                var index = currentRawText.indexOf(key)
                while (index != -1) {
                    addStyle(
                        style = SpanStyle(color = color, fontWeight = FontWeight.Bold),
                        start = index,
                        end = index + key.length
                    )
                    index = currentRawText.indexOf(key, index + 1)
                }
            }
        }
    }

    val cyberFont = FontFamily(Font(R.font.cyber_font_regular, FontWeight.Normal))

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
            onCharAdded()
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
            text = annotatedText, // On utilise la version colorée
            style = LocalTextStyle.current.copy(
                fontFamily = cyberFont,
                fontSize = fontSize,
                lineHeight = fontSize * 1.4f,
                letterSpacing = 1.sp,
                color = Color.White.copy(alpha = 0.9f) // Couleur de base si aucun highlight
            ),
            // IMPORTANT : Pas de 'color = Color.White' ici pour laisser les SpanStyles respirer
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