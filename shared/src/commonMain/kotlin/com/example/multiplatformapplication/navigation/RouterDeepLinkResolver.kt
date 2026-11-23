package com.example.multiplatformapplication.navigation

import androidx.navigation3.runtime.NavKey

data class DeepLinkMatch(
    val backStack: List<NavKey>,
    val sendResult: Boolean,
)

class RouterDeepLinkResolver {
    fun resolve(uriString: String): DeepLinkMatch? {
        val normalized = uriString.replace("/#/", "/")
        val isJacpotScheme = normalized.startsWith("jacpot://marketplace/login")
        val isLocalWebLogin = normalized.startsWith("http://localhost:8080/login")

        if (!isJacpotScheme && !isLocalWebLogin) return null

        val sendResult = normalized.substringAfter("?", missingDelimiterValue = "")
            .split("&")
            .any { it.equals("sendResult=true", ignoreCase = true) }

        return DeepLinkMatch(
            backStack = listOf(LoginScreenKey),
            sendResult = sendResult,
        )
    }
}