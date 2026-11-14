package com.example.multiplatformapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.multiplatformapplication.model.Request
import com.example.multiplatformapplication.ui.screens.CreateRequestScreen
import com.example.multiplatformapplication.ui.screens.CreateSellItemForm
import com.example.multiplatformapplication.ui.screens.Listing
import com.example.multiplatformapplication.ui.screens.LoginScreen
import com.example.multiplatformapplication.ui.screens.MarketplaceScreen
import com.example.multiplatformapplication.ui.screens.PlaceholderScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Serializable
data object LoginScreenKey : NavKey

@Serializable
data object HomeScreenKey : NavKey

@Serializable
data object MarketplaceScreenKey : NavKey

@Serializable
data object RequestsScreenKey : NavKey

@Serializable
data object ProfileScreenKey : NavKey

@Serializable
data object CreateScreenKey : NavKey

@Serializable
data object CreateRequestScreenKey : NavKey

val backStackConfig =
    SavedStateConfiguration {
        serializersModule =
            SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(LoginScreenKey::class, LoginScreenKey.serializer())
                    subclass(HomeScreenKey::class, HomeScreenKey.serializer())
                    subclass(MarketplaceScreenKey::class, MarketplaceScreenKey.serializer())
                    subclass(RequestsScreenKey::class, RequestsScreenKey.serializer())
                    subclass(ProfileScreenKey::class, ProfileScreenKey.serializer())
                    subclass(CreateScreenKey::class, CreateScreenKey.serializer())
                    subclass(CreateRequestScreenKey::class, CreateRequestScreenKey.serializer())
                }
            }
    }

val LocalNavigator =
    compositionLocalOf<Navigator> {
        error("No Navigator found! Wrap your UI with CompositionLocalProvider.")
    }

@Composable
fun Router() {
    val backStack = rememberNavBackStack(backStackConfig, LoginScreenKey)
    val navigator = remember(backStack) { Navigator(backStack) }
    val createdListings = remember { mutableStateListOf<Listing>() }
    val createdRequests = remember { mutableStateListOf<Request>() } // Browse Requests will read this

    CompositionLocalProvider(LocalNavigator provides navigator) {
        NavDisplay(
            backStack = backStack,
            onBack = { navigator.pop() },
            entryProvider =
                entryProvider {
                    entry<LoginScreenKey> { LoginScreen() }
                    entry<HomeScreenKey> { Shell(HomeScreenKey, navigator) { PlaceholderScreen("Home") } }
                    entry<MarketplaceScreenKey> {
                        Shell(MarketplaceScreenKey, navigator) { MarketplaceScreen(createdListings = createdListings) }
                    }
                    entry<RequestsScreenKey> {
                        Shell(RequestsScreenKey, navigator) {
                            PlaceholderScreen(
                                "Requests (${createdRequests.size} posted)",
                                "Post a request",
                            ) { navigator.navigate(CreateRequestScreenKey) }
                        }
                    }
                    entry<CreateRequestScreenKey> {
                        CreateRequestScreen(onSubmit = { request ->
                            createdRequests.add(request)
                            navigator.pop()
                        })
                    }
                    entry<ProfileScreenKey> { Shell(ProfileScreenKey, navigator) { PlaceholderScreen("Profile") } }
                    entry<CreateScreenKey> {
                        CreateSellItemForm(onItemCreated = { item ->
                            createdListings.add(
                                Listing(
                                    id = 1000 + createdListings.size,
                                    title = item.title,
                                    price = "\$${item.price}",
                                    category = item.category,
                                    seller = "You",
                                ),
                            )
                            navigator.pop()
                        })
                    }
                },
        )
    }
}

// Tabs sit one level above Home: switching pops back to Home first so the back stack never grows.
@Composable
private fun Shell(
    current: NavKey,
    navigator: Navigator,
    content: @Composable () -> Unit,
) = AdaptiveNavigation(
    selected = current,
    onSelect = { key ->
        if (key != current) {
            navigator.popUntil(HomeScreenKey)
            if (key != HomeScreenKey) navigator.navigate(key)
        }
    },
    content = content,
)
