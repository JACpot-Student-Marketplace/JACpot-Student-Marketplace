package com.example.multiplatformapplication

import com.example.multiplatformapplication.navigation.Router
import com.example.multiplatformapplication.ui.theme.JACpotTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    JACpotTheme {
        // Nothing else paints the theme background.
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Router()
        }
    }
}
