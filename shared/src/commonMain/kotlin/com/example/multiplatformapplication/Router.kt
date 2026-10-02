package com.example.multiplatformapplication

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
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Serializable
data object LoginScreenKey : NavKey

@Serializable
data object MarketplaceScreenKey : NavKey

@Serializable
data object CreateScreenKey : NavKey

val backStackConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(LoginScreenKey::class, LoginScreenKey.serializer())
            subclass(MarketplaceScreenKey::class, MarketplaceScreenKey.serializer())
            subclass(CreateScreenKey::class, CreateScreenKey.serializer())
        }
    }
}

val LocalNavigator = compositionLocalOf<Navigator> {
    error("No Navigator found! Wrap your UI with CompositionLocalProvider.")
}

@Composable
fun Router() {
    val backStack = rememberNavBackStack(backStackConfig, LoginScreenKey)
    val navigator = remember(backStack) { Navigator(backStack) }
    val createdListings = remember { mutableStateListOf<Listing>() }

    CompositionLocalProvider(LocalNavigator provides navigator) {
        NavDisplay(
            backStack = backStack,
            onBack = { navigator.pop() },
            entryProvider = entryProvider {
                entry<LoginScreenKey> { LoginScreen() }
                entry<MarketplaceScreenKey> { MarketplaceScreen(createdListings = createdListings) }
                entry<CreateScreenKey> {
                    CreateSellItemForm(onItemCreated = { item ->
                        createdListings.add(
                            Listing(
                                id = 1000 + createdListings.size,
                                title = item.title,
                                price = "\$${item.price}",
                                category = item.category,
                                seller = "You"
                            )
                        )
                        navigator.pop()
                    })
                }
            }
        )
    }
}
