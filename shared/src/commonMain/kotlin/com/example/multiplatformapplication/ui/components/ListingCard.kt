package com.example.multiplatformapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.multiplatformapplication.ui.screens.Listing

/**
 * One row in the marketplace list.
 *
 * [listing] is a row the parent already decided to show. This composable does
 * not read the search text or the selected category. [modifier] is applied to
 * the card so the list can make each row full width.
 */
@Composable
fun ListingCard(
    listing: Listing,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Column(Modifier.padding(12.dp)) {
            Text(listing.title)
            Text("${listing.price} · ${listing.seller}")
        }
    }
}
