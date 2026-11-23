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
import com.example.multiplatformapplication.data.mockRequests
import com.example.multiplatformapplication.model.Request
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
import kotlin.time.Clock
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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

@Composable
fun Router() {
    val backStack = rememberNavBackStack(backStackConfig, LoginScreenKey)
    val navigator = remember(backStack) { Navigator(backStack) }
    val listings = LocalListings.current
    val createdRequests = remember { mutableStateListOf<Request>() }
    val sampleRequests = remember { mockRequests(Clock.System.now().toEpochMilliseconds()) }

    val deepLinkResolver = remember { RouterDeepLinkResolver() }
    var sendResultToLauncher by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        ExternalUriHandler.listener = { uri ->
            deepLinkResolver.resolve(uri)?.let { match ->
                sendResultToLauncher = match.sendResult
                backStack.clear()
                backStack.addAll(match.backStack)
            }
        }
        onDispose {
            ExternalUriHandler.listener = null
        }
    }

    CompositionLocalProvider(LocalNavigator provides navigator) {
        NavDisplay(
            backStack = backStack,
            onBack = { navigator.pop() },
            entryProvider =
                entryProvider {
                    entry<LoginScreenKey> { LoginScreen() }
                    entry<HomeScreenKey> {
                        Shell(HomeScreenKey, navigator, sendResultToLauncher) {
                            PlaceholderScreen("Home")
                        }
                    }
                    entry<MarketplaceScreenKey> {
                        Shell(MarketplaceScreenKey, navigator, sendResultToLauncher) {
                            MarketplaceScreen()
                        }
                    }
                    entry<RequestsScreenKey> {
                        Shell(RequestsScreenKey, navigator, sendResultToLauncher) {
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
                    entry<ProfileScreenKey> {
                        Shell(ProfileScreenKey, navigator, sendResultToLauncher) {
                            PlaceholderScreen("Profile")
                        }
                    }
                    entry<CreateScreenKey> {
                        CreateSellItemForm(onCreated = {
                            navigator.popUntil(HomeScreenKey)
                            navigator.navigate(MarketplaceScreenKey)
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
    sendResult: Boolean = false,
    content: @Composable () -> Unit,
) {
    val sendResultAndClose = rememberResultResponder()

    Column {
        if (sendResult) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Opened from JAC Planner",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Button(onClick = {
                        sendResultAndClose("JACpot Verified: User logged in (${current::class.simpleName})")
                    }) {
                        Text("Return Result")
                    }
                }
            }
        }

        AdaptiveNavigation(
            selected = current,
            onSelect = { key ->
                if (key != current) {
                    navigator.popUntil(HomeScreenKey)
                    if (key != HomeScreenKey) navigator.navigate(key)
                }
            },
            content = content,
        )
    }
}
