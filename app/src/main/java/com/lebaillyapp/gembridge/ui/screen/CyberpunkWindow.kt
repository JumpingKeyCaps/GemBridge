package com.lebaillyapp.gembridge.ui.screen

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.lebaillyapp.gembridge.R
import com.lebaillyapp.gembridge.ui.component.AiResponseDisplay
import kotlinx.coroutines.launch

// C'est le nouveau composant qui va envelopper tout ton écran ou ta zone de discussion
@Composable
fun CyberpunkWindow(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    // Charger le nouveau shader
    val shaderSource = remember {
        context.resources.openRawResource(R.raw.crt_lens).use { it.bufferedReader().readText() }
    }
    val runtimeShader = remember(shaderSource) { RuntimeShader(shaderSource) }

    // Temps global pour les animations du shader (scanlines, futurs glitches)
    val totalTime by rememberInfiniteTransition().animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(100000, easing = LinearEasing))
    )

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize() // S'étend sur tout l'espace disponible
            .background(Color.Black) // Fond noir pour simuler l'extinction des bords du tube
            .graphicsLayer {
                runtimeShader.setFloatUniform("size", size.width, size.height)
                runtimeShader.setFloatUniform("time", totalTime)

                renderEffect = RenderEffect.createRuntimeShaderEffect(
                    runtimeShader, "composable"
                ).asComposeRenderEffect()
            }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState) // Permet le scroll
                .padding(horizontal = 16.dp, vertical = 40.dp) // Un peu de marge pour pas coller aux bords distordus
        ) {
            AiResponseDisplay(
                fullText = """
    [    0.000000] Linux version 6.1.0-gemini-retro (gcc 12.2.0) #42 SMP PREEMPT
    [    0.000012] CPU: ARMv8 rev 4 (v8l) - Cluster 0: 4x Cortex-A710, Cluster 1: 4x Cortex-A510
    [    0.000045] Memory: 16GB LPDDR5 | Available: 12.4GB | Kernel Reserved: 128MB
    [    0.000102] Machine model: Google Pixel 7 Pro - GemBridge Edition
    [    0.000150] percpu: Embedded 20 pages/cpu s53248 r8192 d32768 u81920
    
    [    0.452103] vgaarb: setting as boot device: PCI:0000:00:01.0 (Mobile_Gpu_Neon)
    [    0.512334] Console: switching to colour frame buffer device 240x135
    [    0.620011] i2c_designware: found Plasma_Control_Node at 0x48
    [    0.710221] pinctrl-gemini: 114 pins at 0x00000000f0000000 registered
    
    [    0.892331] Mounting root filesystem (ext4) on /dev/nvme0n1p2...
    [    1.104223] EXT4-fs (nvme0n1p2): mounted filesystem with ordered data mode.
    
    [    1.442885] [SYSTEM] Starting GemBridge-Interface Core Services:
    [  OK  ] Loading Firmware: Neural_Core_v3_Flash.bin...
    [  OK  ] Initializing AGSL_Runtime_Engine...
    [  OK  ] Synchronizing CRT_Lens_Driver (Lens_Anamorph_Correction)...
    [  OK  ] Calibrating RGB_Shift_Buffers (Anaglyph_Active)...
    
    [    1.850221] Dumping Neural Core Registers:
    [    1.850230] R0: 0x00004f21 | R1: 0xffffffff | R2: 0x00000000
    [    1.850242] R3: 0x12a4b8f0 | R4: 0x0000dead | R5: 0x0000beef
    [    1.850255] STATUS: [CRITICAL_SYNC_STABLE]
    
    [    1.990234] Checking Thermal States...
    [ WARN ] Cluster_0: 82°C - Applying passive cooling.
    [ WARN ] Plasma_Distortion: High thermal signature detected.
    [ INFO ] Adjusting Jitter_Sync: Horizontal sync stabilized at 15.75 kHz.
    
    [    2.102334] User context detected: 'Gemini_Dev_Mode'
    [    2.105442] Security policy: SELinux Enforcing (Retro_Permissive_v2)
    [    2.330119] Network: eth0 link up, 1000 Mbps, full duplex.
    
    [    2.440921] Loading UI components...
    [##########..........] 50% - Scanning Font_Atlas...
    [###############.....] 75% - Compiling AGSL_Shaders...
    [####################] 100% - Render_Target_Ready.
    
    [    2.800112] Background Tasks:
    [ RUN ] crt_flicker_daemon (PID: 104)
    [ RUN ] jitter_generator (PID: 105)
    [ RUN ] anaglyph_multiplexer (PID: 106)
    
    [    3.120554] --- BEGIN LOG STREAM ---
    [    3.130221] tty1: login: root
    [    3.140232] Password: ****************
    [    3.150110] Access Granted. Welcome to Gemini OS v3.0.
    
    [    3.200441] Last login: Fri Feb 20 02:00:15 2026 from 127.0.0.1
    [    3.210992] No mail.
    
    [    3.400111] root@gembridge:~# cd projects/shaders/crt_lens
    [    3.500223] root@gembridge:~/projects/shaders/crt_lens# ls -la
    total 42
    drwxr-xr-x 2 root root 4096 Feb 20 02:01 .
    drwxr-xr-x 4 root root 4096 Feb 20 02:00 ..
    -rw-r--r-- 1 root root 8244 Feb 20 02:01 crt_lens.agsl
    -rw-r--r-- 1 root root 1205 Feb 20 02:01 config.json
    
    [    3.800551] root@gembridge:~/projects/shaders/crt_lens# cat config.json
    {
      "anamorphic_correction": true,
      "spherical_ratio": "compensated",
      "anaglyph": {
        "text": 12.0,
        "grid": 4.0
      },
      "jitter": "enabled"
    }
    
    [    4.100992] root@gembridge:~/projects/shaders/crt_lens# ./deploy_to_pixel7.sh
    [ INFO ] Pushing shader to /data/local/tmp/...
    [ INFO ] Restarting SurfaceFlinger...
    [  OK  ] Shader active. Hardware acceleration engaged.
    
    [    4.500000] SYSTEM STABLE. READY FOR INPUT.
    
    root@gembridge:~/shaders# _
""".trimIndent(),
                onCharAdded = {
                    // À chaque nouveau caractère, on descend tout en bas
                    // On utilise launch {} car le scroll est une fonction suspendue
                    scope.launch {
                        scrollState.scrollTo(scrollState.maxValue)
                     }
                },
                charDelay = 20L
            )

            // Petit spacer optionnel à la fin pour pouvoir scroller au-delà du texte
            Spacer(modifier = Modifier.height(250.dp))
        }


    }
}