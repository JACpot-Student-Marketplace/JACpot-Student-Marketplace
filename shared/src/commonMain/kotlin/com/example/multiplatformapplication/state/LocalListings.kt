package com.example.multiplatformapplication.state

import androidx.compose.runtime.staticCompositionLocalOf

val LocalListings =
    staticCompositionLocalOf<ListingsViewModel> {
        error("No ListingsViewModel found! Provide it in App().")
    }
