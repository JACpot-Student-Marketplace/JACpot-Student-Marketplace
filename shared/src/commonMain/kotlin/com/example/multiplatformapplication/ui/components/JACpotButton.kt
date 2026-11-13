package com.example.multiplatformapplication.ui.components

import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Emphasis levels: Filled for the main action, Outlined for secondary, Text for low-key actions. */
enum class ButtonStyle { Filled, Outlined, Text }

/** The app's one button. Every style gets a 48dp minimum height so it is a comfortable touch target. */
@Composable
fun JACpotButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ButtonStyle = ButtonStyle.Filled,
    enabled: Boolean = true,
) {
    val sized = modifier.heightIn(min = 48.dp)
    when (style) {
        ButtonStyle.Filled -> Button(onClick, sized, enabled) { Text(text) }
        ButtonStyle.Outlined -> OutlinedButton(onClick, sized, enabled) { Text(text) }
        ButtonStyle.Text -> TextButton(onClick, sized, enabled) { Text(text) }
    }
}
