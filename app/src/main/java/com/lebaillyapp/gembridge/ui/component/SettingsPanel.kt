package com.lebaillyapp.gembridge.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * # SettingsPanel
 * * A UI panel that provides controls for configuring Gemini AI model parameters.
 * This panel is designed to be revealed or hidden via a sliding animation.
 * * It includes controls for:
 * - Model selection (Display only)
 * - Generation parameters (Temperature, Top-K, Top-P)
 * - System Context (instructions for the AI)
 * - Multi-agent workflow toggling
 * * @param modifier Modifier for the panel's layout.
 * @param temp Controls the randomness of the output. Higher values (up to 2.0) make it more creative.
 * @param topK Limits the model to pick from the top K most likely words.
 * @param topP Nucleus sampling; picks from the smallest set of words whose cumulative probability exceeds P.
 * @param systemContext The initial instructions given to the AI to define its role or constraints.
 */
@Composable
fun SettingsPanel(
    modifier: Modifier = Modifier,
    temp: Float = 0.7f,// Idéalement, ces valeurs viendront de ton ChatState via le ViewModel plus tard
    topK: Float = 40f,
    topP: Float = 0.95f,
    systemContext: String = ""
) {
    // On ajoute un scroll vertical au cas où l'écran est petit ou le contexte long
    Column(
        modifier = modifier
            .background(Color(0xFF0F1115))
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "GemBridge Configuration",
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xffedff61)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // --- SECTION : PARAMÈTRES GÉNÉRAUX ---
        SettingItem(label = "Model", value = "Gemini 1.5 Flash")

        // --- SECTION : SLIDERS (Temp, TopK, TopP) ---
        // Température
        SliderSetting(
            label = "Temperature",
            value = temp,
            valueRange = 0f..2f,
            displayValue = String.format("%.1f", temp)
        )

        // Top-K
        SliderSetting(
            label = "Top-K",
            value = topK,
            valueRange = 1f..100f,
            displayValue = topK.toInt().toString()
        )

        // Top-P
        SliderSetting(
            label = "Top-P",
            value = topP,
            valueRange = 0f..1f,
            displayValue = String.format("%.2f", topP)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- SECTION : SYSTEM CONTEXT ---
        Text(
            text = "System Context",
            color = Color.Gray,
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = systemContext,
            onValueChange = { /* Update VM */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            placeholder = { Text("Ex: Tu es un assistant expert en Kotlin...", color = Color.DarkGray) },
            textStyle = MaterialTheme.typography.bodySmall,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF16191F),
                unfocusedContainerColor = Color(0xFF12141A),
                focusedBorderColor = Color(0xffedff61),
                unfocusedBorderColor = Color.DarkGray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Mode Multi-Agent
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Multi-Agent Workflow", color = Color.White)
            Switch(
                checked = true,
                onCheckedChange = {},
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xffedff61),
                    checkedTrackColor = Color(0xffedff61).copy(alpha = 0.5f)
                )
            )
        }

        // Un petit espace pour ne pas coller au bord bas
        Spacer(modifier = Modifier.height(40.dp))
    }
}

/**
 * # SliderSetting
 * * A reusable component that displays a label, its current value, and a slider.
 * Used for fine-tuning numerical AI parameters.
 * * @param label The name of the parameter.
 * @param value The current state value.
 * @param valueRange The allowed range for the slider.
 * @param displayValue The formatted string representation of the value.
 */
@Composable
fun SliderSetting(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    displayValue: String
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, color = Color.Gray, style = MaterialTheme.typography.labelMedium)
            Text(text = displayValue, color = Color(0xffedff61), style = MaterialTheme.typography.labelMedium)
        }
        Slider(
            value = value,
            onValueChange = { /* Update VM */ },
            valueRange = valueRange,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xffedff61),
                activeTrackColor = Color(0xffedff61),
                inactiveTrackColor = Color.DarkGray
            )
        )
    }
}

/**
 * # SettingItem
 * * A simple row-based display for static configuration settings.
 * * @param label The descriptive label of the setting.
 * @param value The current value or selection.
 */
@Composable
fun SettingItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, color = Color.Gray, style = MaterialTheme.typography.labelMedium)
        Text(text = value, color = Color.White, style = MaterialTheme.typography.bodyLarge)
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp), color = Color.DarkGray)
    }
}