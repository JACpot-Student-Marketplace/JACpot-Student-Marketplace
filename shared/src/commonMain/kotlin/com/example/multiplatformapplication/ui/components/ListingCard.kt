package com.example.multiplatformapplication.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.multiplatformapplication.model.Listing
import com.example.multiplatformapplication.model.formatPostedAgo
import com.example.multiplatformapplication.model.formatPrice
import com.example.multiplatformapplication.model.label
import kotlin.time.Clock


@Composable
fun ListingCard(
    listing: Listing,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    photoAspectRatio: Float = 1f,
) {
    val now = Clock.System.now().toEpochMilliseconds() //get the current time

    Card(onClick = onClick, modifier = modifier) {
        Box {
            ListingPhoto(
                url = listing.photoUrls.firstOrNull(),
                description = listing.title,
                aspectRatio = photoAspectRatio,
            )
            PriceBadge(formatPrice(listing.priceCents),
            modifier.align(Alignment.TopStart).padding(12.dp))
        }
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text(
              listing.title,
              style = MaterialTheme.typography.titleMedium,
              maxLines = 2,
              overflow = TextOverflow.Ellipsis,
          )
            Text(
                listOfNotNull(listing.condition.label, listing.courseCode).joinToString(" • "),

                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                formatPostedAgo(listing.createdAt, now),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun PriceBadge(
    price: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        Text(price, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.titleMedium)
    }
}
