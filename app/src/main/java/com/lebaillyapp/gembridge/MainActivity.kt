package com.lebaillyapp.gembridge

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.VibratorManager
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.lebaillyapp.gembridge.domain.model.ChatState
import com.lebaillyapp.gembridge.ui.component.AiResponseDisplay
import com.lebaillyapp.gembridge.ui.component.ChatBackgroundWithBlob
import com.lebaillyapp.gembridge.ui.screen.AiChatScreen

import com.lebaillyapp.gembridge.ui.screen.ChatScreenV2
import com.lebaillyapp.gembridge.ui.screen.CyberpunkWindow
import com.lebaillyapp.gembridge.ui.theme.GemBridgeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. On initialise l'instance
        super.onCreate(savedInstanceState)
        // 2. On installe le splash
        val splashScreen = installSplashScreen()
        // 3. La condition de maintien ( timer de 2s) // TEST ONLY !
        var isReady = false
        splashScreen.setKeepOnScreenCondition { !isReady }
        lifecycleScope.launch {
            delay(800)
            isReady = true
        }
        // 4. L'animation de sortie (le zoom)
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            triggerDoubleTapHaptic() // micro vivration prenium
            // 1. On sépare l'icône du fond (View)
            val iconView = splashScreenView.iconView
            val backgroundView = splashScreenView.view // Le fond noir

            // 2. L'icône s'envole (Anticipate)
            val scaleX = ObjectAnimator.ofFloat(iconView, View.SCALE_X, 1f, 2f)
            val scaleY = ObjectAnimator.ofFloat(iconView, View.SCALE_Y, 1f, 2f)
            val iconAlpha = ObjectAnimator.ofFloat(iconView, View.ALPHA, 1f, 0f)

            // et il disparaît plus lentement pour couvrir l'UI
            val backAlpha = ObjectAnimator.ofFloat(backgroundView, View.ALPHA, 1f, 0f).apply {
                duration = 800L
                startDelay = 400L
            }

            AnimatorSet().apply {
                duration = 1000L
                interpolator = AnticipateInterpolator()
                playTogether(scaleX, scaleY, iconAlpha, backAlpha)
                doOnEnd { splashScreenView.remove() }
                start()
            }


        }
        // 5. Le reste de l' UI
        enableEdgeToEdge()
        setContent {
            GemBridgeTheme {

                // On anime l'entrée de toute l'UI
                val alphaAnim = remember { androidx.compose.animation.core.Animatable(0f) }
                LaunchedEffect(Unit) {
                    // On attend un peu que le splash commence à s'envoler
                    delay(500)
                    alphaAnim.animateTo(1f, animationSpec = tween(600))
                }


                Surface(color = MaterialTheme.colorScheme.background) {

                  //  ChatScreenV2()

                    //todo - test agsl shader UI

                    /**
                    // On crée un état local pour le test
                    var testIndex by remember { mutableStateOf(0) }
                    val dummyState = remember(testIndex) {
                        when (testIndex) {
                            0 -> ChatState(isLoading = false, error = null)     // Idle (Bleu)
                            1 -> ChatState(isLoading = true, error = null)      // Loading (Violet)
                            else -> ChatState(isLoading = false, error = "Erreur") // Error (Rouge)
                        }
                    }
                    ChatBackgroundWithBlob(
                        chatState = dummyState,
                        onBgClick = {
                            testIndex = (testIndex + 1) % 3
                        }
                    )

                    */

                    //todo - demo ai sreen display
                //    AiChatScreen()



                    CyberpunkWindow()



                }
            }
        }

    }


    private fun triggerDoubleTapHaptic() {
        val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        val vibrator = vibratorManager.defaultVibrator

        // Pattern : [Attente, Vibre, Attente, Vibre]
        // Intensités : [0, 150, 0, 150] (sur 255)
        val effect = VibrationEffect.createWaveform(
            longArrayOf(0, 15, 60, 15),
            intArrayOf(0, 120, 0, 120),
            -1
        )
        vibrator.vibrate(effect)
    }

}
