package com.example.multiplatformapplication.navigation

import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.deeplink.DeepLinkMatcher
import androidx.navigation3.runtime.deeplink.DeepLinkRequest
import androidx.navigation3.runtime.deeplink.DeepLinkUri
import androidx.navigation3.runtime.deeplink.UriDeepLinkMatcher
import kotlinx.serialization.serializer

class RouterDeepLinkResolver {
    private val listingsMatcher: DeepLinkMatcher<*, *> =
        UriDeepLinkMatcher(
            uriPattern = DeepLinkUri("jacpot://marketplace/listings?sendResult={sendResult}"),
            serializer = serializer<MarketplaceScreenKey>(),
        )

    private val webListingsMatcher: DeepLinkMatcher<*, *> =
        UriDeepLinkMatcher(
            uriPattern = DeepLinkUri("http://localhost:8080/listings?sendResult={sendResult}"),
            serializer = serializer<MarketplaceScreenKey>(),
        )

    private val matchers = listOf(listingsMatcher, webListingsMatcher)

    fun resolve(uriString: String): List<NavKey>? {
        val normalizedUri = uriString.replace("/#/", "/")
        val request = DeepLinkRequest(uri = normalizedUri)
        for (matcher in matchers) {
            val matchResult = matcher.match(request) ?: continue
            val targetKey = matchResult.key as? NavKey ?: continue
            return listOf(HomeScreenKey, targetKey)
        }
        return null
    }
}
