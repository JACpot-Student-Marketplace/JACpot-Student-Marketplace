package com.example.multiplatformapplication.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.multiplatformapplication.model.ItemCondition
import com.example.multiplatformapplication.model.ListingCategory
import com.example.multiplatformapplication.model.ListingStatus
import com.example.multiplatformapplication.model.label
import com.example.multiplatformapplication.navigation.CreateRequestScreenKey
import com.example.multiplatformapplication.navigation.CreateScreenKey
import com.example.multiplatformapplication.navigation.LocalNavigator
import com.example.multiplatformapplication.state.ListingSort
import com.example.multiplatformapplication.state.LocalListings
import com.example.multiplatformapplication.state.queryListings
import com.example.multiplatformapplication.ui.components.ButtonStyle
import com.example.multiplatformapplication.ui.components.EmptyFeed
import com.example.multiplatformapplication.ui.components.FilterMenu
import com.example.multiplatformapplication.ui.components.JACpotButton
import com.example.multiplatformapplication.ui.components.ListingCard
import com.example.multiplatformapplication.ui.components.ListingDetailsDialog
import com.example.multiplatformapplication.ui.components.SearchBar

/**
 * One student item for sale. [category] is compared to the selected chip,
 * and [title] is what the search field matches against.
 */
data class Listing(
    val id: Int,
    val title: String,
    val price: String,
    val category: String,
    val seller: String,
)

private val sampleListings =
    listOf(
        Listing(1, "Calculus Textbook", "$40", "Textbooks", "Maya"),
        Listing(2, "Intro to Java", "$25", "Textbooks", "Omar"),
        Listing(3, "Campus Hoodie", "$18", "Clothes", "Priya"),
        Listing(4, "Winter Jacket", "$35", "Clothes", "Leo"),
        Listing(5, "MacBook", "$120", "Tech", "James"),
        Listing(6, "Drake Concert pair", "$200", "Tickets", "Leo"),
        Listing(7, "Mario Odyssey", "$40", "Gaming", "Omar"),
    )

/**
 * Single marketplace screen. The search text and selected category live here
 * so both filters share one source of truth and both survive rotation.
 */
@Composable
fun MarketplaceScreen(modifier: Modifier = Modifier) {
    val navigator = LocalNavigator.current
    val listings = LocalListings.current
    var categoryName by rememberSaveable { mutableStateOf<String?>(null) }
    var conditionName by rememberSaveable { mutableStateOf<String?>(null) }
    var sortName by rememberSaveable { mutableStateOf(ListingSort.NEWEST.name) }
    val category = categoryName?.let { ListingCategory.valueOf(it) }
    val condition = conditionName?.let { ItemCondition.valueOf(it) }
    val sort = ListingSort.valueOf(sortName)
    var query by rememberSaveable { mutableStateOf("") }

    val active = listings.activeListings
    val visible = queryListings(active, query, category, condition, sort)
    val filtersApplied =
        query.isNotBlank() || category != null || condition != null || sort != ListingSort.NEWEST
    val resetFilters = {
        query = ""
        categoryName = null
        conditionName = null
        sortName = ListingSort.NEWEST.name
    }

    var openListingId by rememberSaveable { mutableStateOf<String?>(null) }

    BoxWithConstraints(modifier.fillMaxSize()) {
        val wide = maxWidth >= 600.dp
        LazyVerticalGrid(
            columns = if (wide) GridCells.Adaptive(240.dp) else GridCells.Fixed(1),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(if (wide) 24.dp else 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                MarketplaceHeader(onSell = { navigator.navigate(CreateScreenKey) })
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SearchBar(
                        query = query,
                        onQueryChange = { query = it },
                        placeholder = "Search for your desired item",
                        modifier = Modifier.fillMaxWidth(),
                    )
                    CategoryChips(selected = category, onSelect = { categoryName = it?.name })

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        FilterMenu(
                            label = "Condition: ${condition?.label ?: "Any"}",
                            options = listOf(null) + ItemCondition.entries,
                            optionLabel = { it?.label ?: "Any condition" },
                            onSelect = { conditionName = it?.name },
                        )
                        FilterMenu(
                            label = sort.label,
                            options = ListingSort.entries,
                            optionLabel = { it?.label ?: "Newest first" },
                            onSelect = { sortName = it.name },
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "${visible.size} of ${active.size} listings",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        if (filtersApplied) {
                            JACpotButton("Reset filters", onClick = resetFilters, style = ButtonStyle.Text)
                        }
                    }
                }
            }
            if (active.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    EmptyFeed("No listings yet", "Be the first to sell something to another JAC student.")
                }
            } else if (visible.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    EmptyFeed(
                        title = "No matching listings",
                        message = "Try another search, or post a request so sellers can find you.",
                        actionText = "Post a request",
                        onAction = { navigator.navigate(CreateRequestScreenKey) },
                    )
                }
            }
            items(visible, key = { it.id }) { listing ->
                ListingCard(
                    listing = listing,
                    onClick = { openListingId = listing.id },
                    photoAspectRatio = if (wide) 1f else 4f / 3f,
                )
            }
        }
    }

    val openListing =
        openListingId
            ?.let { listings.findListing(it) }
            ?.takeIf { it.status == ListingStatus.ACTIVE }
    if (openListing != null) {
        ListingDetailsDialog(
            listing = openListing,
            seller = listings.seller(openListing),
            isOwner = listings.isOwner(openListing),
            onDismiss = { openListingId = null },
            onMarkSold = {
                listings.markSold(openListing.id)
                openListingId = null
            },
            onDelete = {
                listings.deleteListing(openListing.id)
                openListingId = null
            },
        )
    }
}

@Composable
private fun MarketplaceHeader(onSell: () -> Unit) {
    FlowRow(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Browse Items", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            Text("Marketplace", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Find what you need from other JAC students!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        JACpotButton("+ Sell an item", onClick = onSell)
    }
}

@Composable
private fun CategoryChips(
    selected: ListingCategory?,
    onSelect: (ListingCategory?) -> Unit,
) {
    Row(
        Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilterChip(selected = selected == null, onClick = { onSelect(null) }, label = { Text("All items") })
        ListingCategory.entries.forEach { category ->
            FilterChip(
                selected = selected == category,
                onClick = { onSelect(category) },
                label = { Text(category.label) },
            )
        }
    }
}
