package com.example.multiplatformapplication

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

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
fun MarketplaceScreen(
    modifier: Modifier = Modifier,
    createdListings: List<Listing> = emptyList(),
) {
    val navigator = LocalNavigator.current
    // rememberSaveable keeps these across rotation. remember would reset them
    // when the activity is recreated, and the list would jump back to every item.
    var query by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf("All") }

    // A plain List inside mutableStateOf is not written into the rotation bundle.
    // listSaver stores each name and rebuilds the list after the activity is recreated.
    val categories =
        rememberSaveable(
            saver =
                listSaver<SnapshotStateList<String>, String>(
                    save = { it.toList() },
                    restore = { it.toMutableStateList() },
                ),
        ) {
            mutableStateListOf("All", "Textbooks", "Clothes")
        }
    var addingCategory by rememberSaveable { mutableStateOf(false) }
    var newCategoryName by rememberSaveable { mutableStateOf("") }

    // Blank text does not add a chip. Enter appends the name and the plus moves to the end.
    fun commitCategory() {
        val trimmed = newCategoryName.trim()
        if (trimmed.isEmpty()) return
        categories.add(trimmed)
        newCategoryName = ""
        addingCategory = false
    }

    // Recomputed on every composition from the fixed catalog. A keystroke or
    // chip tap changes state, recomposes this function, and produces a new list.
    val visibleListings =
        (createdListings + sampleListings).filter { listing ->
            val matchesCategory = selectedCategory == "All" || listing.category == selectedCategory
            val matchesQuery =
                query.isBlank() ||
                    listing.title.contains(query.trim(), ignoreCase = true)
            matchesCategory && matchesQuery
        }

    createdListings.forEach { listing ->
        if (listing.category !in categories) categories.add(listing.category)
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Marketplace", modifier = Modifier.weight(1f))
            Button(onClick = { navigator.navigate(CreateScreenKey) }) { Text("Create listing") }
        }

        MarketplaceSearchBar(
            query = query,
            onQueryChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
        )

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            categories.forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category) },
                )
            }
            if (addingCategory) {
                OutlinedTextField(
                    value = newCategoryName,
                    onValueChange = { newCategoryName = it },
                    modifier = Modifier.width(160.dp),
                    singleLine = true,
                    placeholder = { Text("Category") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { commitCategory() }),
                )
            } else {
                Button(onClick = { addingCategory = true }) { Text("Add category") }
            }
        }

        if (visibleListings.isEmpty()) {
            Text("No listings match your search.")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
            ) {
                items(visibleListings, key = { it.id }) { listing ->
                    ListingCard(
                        listing = listing,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}
