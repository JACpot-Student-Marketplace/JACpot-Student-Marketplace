package com.example.multiplatformapplication.navigation

import androidx.compose.runtime.Composable

@Composable
expect fun rememberResultResponder(): (resultData: String) -> Unit