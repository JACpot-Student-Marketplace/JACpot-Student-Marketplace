package com.example.multiplatformapplication

import androidx.compose.runtime.Composable

@Composable
expect fun rememberResultResponder(): (resultData: String) -> Unit
