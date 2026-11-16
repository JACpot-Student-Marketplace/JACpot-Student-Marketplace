package com.example.multiplatformapplication.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.multiplatformapplication.data.mockUsers
import com.example.multiplatformapplication.model.Request
import com.example.multiplatformapplication.model.formatPrice
import com.example.multiplatformapplication.model.formatTimeAgo
import com.example.multiplatformapplication.model.label
import com.example.multiplatformapplication.ui.screens.formatDate

// shortcut: poster comes from mock users, swap for a real user lookup with auth.
private fun posterName(ownerId: String): String =
    if (ownerId == "me") {
        "you"
    } else {
        mockUsers.firstOrNull { it.id == ownerId }?.let { "${it.firstName} ${it.lastName}" } ?: "a JAC student"
    }

/** Full information of one request, shown over the feed like the listing details dialog. */
@Composable
fun RequestDetailsDialog(
    request: Request,
    now: Long,
    onDismiss: () -> Unit,
) {
    val pills =
        listOfNotNull(
            request.courseCode,
            request.neededBy?.let { "Needed by ${formatDate(it)}" },
            "Expires ${formatDate(request.expiresAt)}",
        )
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(request.status.label.uppercase(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text(request.title)
                Text(
                    "Requested by ${posterName(request.ownerId)} · posted ${formatTimeAgo(request.createdAt, now).lowercase()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    pills.forEach { pill ->
                        Surface(shape = MaterialTheme.shapes.small, color = MaterialTheme.colorScheme.surfaceContainerHighest) {
                            Text(pill, Modifier.padding(horizontal = 12.dp, vertical = 8.dp), style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Label("MAXIMUM BUDGET")
                    Text(
                        request.maxBudgetCents?.let { formatPrice(it) } ?: "Not specified",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                Surface(shape = MaterialTheme.shapes.medium, color = MaterialTheme.colorScheme.surfaceContainerLow) {
                    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Label("DESCRIPTION")
                        Text(request.description, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        },
        confirmButton = { JACpotButton("Close", onClick = onDismiss, style = ButtonStyle.Text) },
    )
}

@Composable
private fun Label(text: String) = Text(text, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
