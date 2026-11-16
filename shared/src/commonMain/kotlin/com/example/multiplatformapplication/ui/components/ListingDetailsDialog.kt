package com.example.multiplatformapplication.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.multiplatformapplication.model.Listing
import com.example.multiplatformapplication.model.User
import com.example.multiplatformapplication.model.formatPostedAgo
import com.example.multiplatformapplication.model.formatPrice
import com.example.multiplatformapplication.model.label
import kotlin.time.Clock

@Composable
fun ListingDetailsDialog(
    listing: Listing,
    seller: User?,
    isOwner: Boolean,
    onDismiss: () -> Unit,
    onMarkSold: () -> Unit,
    onDelete: () -> Unit,
    onMessageSeller: (() -> Unit)? = null,
    onEdit: (() -> Unit)? = null,
) {
    var confirmDelete by rememberSaveable(listing.id) { mutableStateOf(false) }

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Delete listing?") },
            text = { Text("Remove \"${listing.title}\" from the marketplace? This cannot be undone.") },
            confirmButton = { JACpotButton("Delete", onClick = onDelete, style = ButtonStyle.Danger) },
            dismissButton = { JACpotButton("Cancel", onClick = { confirmDelete = false }, style = ButtonStyle.Text) },
        )
    } else {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { DetailsHeader(listing, seller) },
            text = {
                ListingInformation(
                    listing = listing,
                    isOwner = isOwner,
                    chatReady = onMessageSeller != null,
                    onEdit = onEdit,
                    onMarkSold = onMarkSold,
                    onRequestDelete = { confirmDelete = true },
                )
            },
            confirmButton = {
                if (isOwner) {
                    JACpotButton("Close", onClick = onDismiss, style = ButtonStyle.Text)
                } else {
                    JACpotButton(
                        "Message seller",
                        onClick = { onMessageSeller?.invoke() },
                        enabled = onMessageSeller != null,
                    )
                }
            },
            dismissButton =
                if (isOwner) {
                    null
                } else {
                    { JACpotButton("Close", onClick = onDismiss, style = ButtonStyle.Text) }
                },
        )
    }
}

@Composable
private fun DetailsHeader(
    listing: Listing,
    seller: User?,
) {
    val now = Clock.System.now().toEpochMilliseconds()
    val sellerName = seller?.let { "${it.firstName} ${it.lastName}" } ?: "a JAC student"
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            listing.category.label.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(listing.title)
        Text(
            "Sold by $sellerName · posted ${formatPostedAgo(listing.createdAt, now).lowercase()}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ListingInformation(
    listing: Listing,
    isOwner: Boolean,
    chatReady: Boolean,
    onEdit: (() -> Unit)?,
    onMarkSold: () -> Unit,
    onRequestDelete: () -> Unit,
) {
    val pills =
        listOfNotNull(
            listing.condition.label,
            listing.courseCode,
            listing.author?.let { "By $it" },
            listing.releaseDate?.let { "Released $it" },
            listing.accessCodeUsed?.let { if (it) "Access code used" else "Access code unused" },
        )

    Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        PhotoGallery(listing)

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            pills.forEach { DetailPill(it) }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            SectionLabel("PRICE")
            Text(
                formatPrice(listing.priceCents),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Surface(shape = MaterialTheme.shapes.medium, color = MaterialTheme.colorScheme.surfaceContainerLow) {
            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionLabel("DESCRIPTION")
                Text(listing.description, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            SectionLabel("PICKUP")
            Text(
                "On campus. You and the seller can agree on a meetup location in the chat.",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        if (isOwner) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                SectionLabel("MANAGE YOUR LISTING")
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    JACpotButton(
                        "Edit",
                        onClick = { onEdit?.invoke() },
                        style = ButtonStyle.Outlined,
                        enabled = onEdit != null,
                    )
                    JACpotButton("Mark as sold", onClick = onMarkSold, style = ButtonStyle.Outlined)
                    JACpotButton("Delete", onClick = onRequestDelete, style = ButtonStyle.Danger)
                }
            }
        } else if (!chatReady) {
            Text(
                "Messaging the seller opens here once chat is ready.",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun PhotoGallery(listing: Listing) {
    val photos = listing.photoUrls
    var selected by rememberSaveable(listing.id) { mutableIntStateOf(0) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box {
            ListingPhoto(
                url = photos.getOrNull(selected),
                description = "Photo ${selected + 1} of ${listing.title}",
                modifier = Modifier.clip(MaterialTheme.shapes.medium),
                aspectRatio = 4f / 3f,
            )
            if (photos.size > 1) {
                Surface(
                    modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp),
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Text(
                        "${selected + 1} / ${photos.size}",
                        Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        }
        if (photos.size > 1) {
            Row(
                Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                photos.forEachIndexed { index, url ->
                    val outline = if (index == selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                    ListingPhoto(
                        url = url,
                        description = "Show photo ${index + 1}",
                        modifier =
                            Modifier
                                .width(64.dp)
                                .clip(MaterialTheme.shapes.small)
                                .border(2.dp, outline, MaterialTheme.shapes.small)
                                .clickable { selected = index },
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(text, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

@Composable
private fun DetailPill(text: String) {
    Surface(shape = MaterialTheme.shapes.small, color = MaterialTheme.colorScheme.surfaceContainerHighest) {
        Text(text, Modifier.padding(horizontal = 12.dp, vertical = 8.dp), style = MaterialTheme.typography.labelMedium)
    }
}
