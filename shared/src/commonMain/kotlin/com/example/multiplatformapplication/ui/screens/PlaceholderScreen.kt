package com.example.multiplatformapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.multiplatformapplication.ui.components.JACpotButton

// shortcut: stand-in until the real Home, Requests and Profile screens land.
@Composable
fun PlaceholderScreen(
    title: String,
    actionText: String? = null,
    onAction: () -> Unit = {},
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(title, style = MaterialTheme.typography.headlineMedium)
        if (actionText != null) JACpotButton(actionText, onClick = onAction)
    }
}
