package com.example.multiplatformapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun ListingPhoto(
    url: String?,
    description: String,
    modifier: Modifier = Modifier,
    aspectRatio: Float = 1f,
) {
    Box(
        modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            .semantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        PhotoPlaceholder()
    }
}

@Composable
private fun PhotoPlaceholder() {
    Icon(
        Icons.Default.ShoppingCart,
        contentDescription = null,
        modifier = Modifier.size(48.dp),
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
