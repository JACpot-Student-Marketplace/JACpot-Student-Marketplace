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
import com.example.multiplatformapplication.auth.AuthController
import com.example.multiplatformapplication.data.mockRequests
import com.example.multiplatformapplication.model.ItemCondition
import com.example.multiplatformapplication.model.ListingCategory
import com.example.multiplatformapplication.model.Request
import com.example.multiplatformapplication.model.label
import com.example.multiplatformapplication.state.LocalListings
import com.example.multiplatformapplication.ui.screens.CreateAccountScreen
import com.example.multiplatformapplication.ui.screens.CreateRequestScreen
import com.example.multiplatformapplication.ui.screens.CreateSellItemForm
import com.example.multiplatformapplication.ui.screens.LoginScreen
import com.example.multiplatformapplication.ui.screens.MarketplaceScreen
import com.example.multiplatformapplication.ui.screens.PlaceholderScreen
import com.example.multiplatformapplication.ui.screens.RequestsScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlin.math.roundToLong
import kotlin.time.Clock

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

@Serializable
data object CreateAccountScreenKey : NavKey

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
                    subclass(CreateAccountScreenKey::class, CreateAccountScreenKey.serializer())
                }
            }
    }

val LocalNavigator =
    compositionLocalOf<Navigator> {
        error("No Navigator found! Wrap your UI with CompositionLocalProvider.")
    }

val LocalAuth =
    compositionLocalOf<AuthController> {
        error("No AuthController found! Wrap your UI with CompositionLocalProvider.")
    }

@Composable
fun Router() {
    val start = if (LocalAuth.current.session == null) LoginScreenKey else HomeScreenKey
    val backStack = rememberNavBackStack(backStackConfig, start)
    val navigator = remember(backStack) { Navigator(backStack) }
    val listings = LocalListings.current
    val createdRequests = remember { mutableStateListOf<Request>() }
    val sampleRequests = remember { mockRequests(Clock.System.now().toEpochMilliseconds()) }

    CompositionLocalProvider(LocalNavigator provides navigator) {
        NavDisplay(
            backStack = backStack,
            onBack = { navigator.pop() },
            entryProvider =
                entryProvider {
                    entry<LoginScreenKey> { LoginScreen() }
                    entry<HomeScreenKey> { Shell(HomeScreenKey, navigator) { PlaceholderScreen("Home") } }
                    entry<MarketplaceScreenKey> {
                        Shell(MarketplaceScreenKey, navigator) { MarketplaceScreen() }
                    }
                    entry<RequestsScreenKey> {
                        Shell(RequestsScreenKey, navigator) {
                            RequestsScreen(
                                requests = createdRequests + sampleRequests,
                                onPostRequest = { navigator.navigate(CreateRequestScreenKey) },
                            )
                        }
                    }
                    entry<CreateRequestScreenKey> {
                        CreateRequestScreen(onSubmit = { request ->
                            createdRequests.add(request)
                            navigator.pop()
                        })
                    }
                    entry<CreateAccountScreenKey> {
                        CreateAccountScreen()
                    }
                    entry<ProfileScreenKey> { Shell(ProfileScreenKey, navigator) { PlaceholderScreen("Profile") } }
                    entry<CreateScreenKey> {
                        CreateSellItemForm(onItemCreated = { item ->
                            listings.addListing(
                                title = item.title,
                                description = item.description,
                                category = ListingCategory.entries.first { it.label == item.category },
                                // Another story changes this later on
                                condition = ItemCondition.GOOD,
                                priceCents = (item.price * 100).roundToLong(),
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
