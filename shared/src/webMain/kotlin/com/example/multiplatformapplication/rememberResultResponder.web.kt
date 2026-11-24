package com.example.multiplatformapplication

import androidx.compose.runtime.Composable
import kotlinx.browser.window

const val CampusBoardCallbackUrl = "http://localhost:8081/#/callback"

@Composable
actual fun rememberResultResponder(): (resultData: String) -> Unit {
    return { resultData ->
        val encodedResult = resultData.replace(" ", "%20")
        window.open("$CampusBoardCallbackUrl?result=$encodedResult", "_self")
    }
}
