package com.example.multiplatformapplication

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multiplatformapplication.navigation.Router
import com.example.multiplatformapplication.state.ListingsViewModel
import com.example.multiplatformapplication.state.LocalListings
import com.example.multiplatformapplication.ui.theme.JACpotTheme

@Composable
@Preview
fun App() {
    val listings = viewModel { ListingsViewModel() }
    JACpotTheme {
        CompositionLocalProvider(LocalListings provides listings) {
            // Nothing else paints the theme background.
            Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                Router()
            }
        }
    }
}
