package com.example.multiplatformapplication.navigation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey

private class Tab(
    val key: NavKey,
    val label: String,
    val icon: ImageVector,
)

// Order is the order on screen: left to right on the bottom bar, top to bottom on the rail.
private val tabs =
    listOf(
        Tab(HomeScreenKey, "Home", Icons.Default.Home),
        Tab(MarketplaceScreenKey, "Listings", Icons.AutoMirrored.Filled.List),
        Tab(RequestsScreenKey, "Requests", Icons.Default.Search),
        Tab(ProfileScreenKey, "Profile", Icons.Default.Person),
    )

/**
 * Bottom bar under 600dp, navigation rail from 600dp up (Profile pinned to the rail's bottom).
 * [content] stays at the same spot in both layouts so its state survives a resize.
 */
@Composable
fun AdaptiveNavigation(
    selected: NavKey,
    onSelect: (NavKey) -> Unit,
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val wide = maxWidth >= 600.dp
        val blue = MaterialTheme.colorScheme.primary
        val pill = MaterialTheme.colorScheme.primaryContainer
        Row(Modifier.fillMaxSize()) {
            if (wide) {
                NavigationRail(
                    modifier = Modifier.width(104.dp),
                    header = {
                        Text(
                            "JACpot",
                            style = MaterialTheme.typography.titleLarge,
                            color = blue,
                            modifier = Modifier.padding(top = 24.dp, bottom = 32.dp),
                        )
                    },
                ) {
                    tabs.forEach { tab ->
                        if (tab.key == ProfileScreenKey) Spacer(Modifier.weight(1f))
                        NavigationRailItem(
                            selected = tab.key == selected,
                            onClick = { onSelect(tab.key) },
                            icon = { Icon(tab.icon, contentDescription = null) },
                            label = { Text(tab.label) },
                            colors =
                                NavigationRailItemDefaults.colors(
                                    selectedIconColor = blue,
                                    selectedTextColor = blue,
                                    indicatorColor = pill,
                                ),
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }
            Column(Modifier.weight(1f).fillMaxHeight()) {
                Column(Modifier.weight(1f)) { content() }
                if (!wide) {
                    NavigationBar {
                        tabs.forEach { tab ->
                            NavigationBarItem(
                                selected = tab.key == selected,
                                onClick = { onSelect(tab.key) },
                                icon = { Icon(tab.icon, contentDescription = null) },
                                label = { Text(tab.label) },
                                colors =
                                    NavigationBarItemDefaults.colors(
                                        selectedIconColor = blue,
                                        selectedTextColor = blue,
                                        indicatorColor = pill,
                                    ),
                            )
                        }
                    }
                }
            }
        }
    }
}
