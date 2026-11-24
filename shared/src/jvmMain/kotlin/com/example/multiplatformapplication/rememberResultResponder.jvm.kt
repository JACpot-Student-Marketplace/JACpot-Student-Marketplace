package com.example.multiplatformapplication

import androidx.compose.runtime.Composable
import java.awt.Desktop
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
actual fun rememberResultResponder(): (resultData: String) -> Unit {
    return { resultData ->
        val encodedResult = URLEncoder.encode(resultData, StandardCharsets.UTF_8.toString())
        val callbackUri = "launcher://callback?result=$encodedResult"
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI(callbackUri))
            } else {
                println("JVM Result Responder: $resultData")
            }
        } catch (_: Exception) {
            println("JVM Result Responder (no OS handler): $resultData")
        }
    }
}
