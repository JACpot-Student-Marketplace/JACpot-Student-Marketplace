package com.example.multiplatformapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.multiplatformapplication.model.Request
import com.example.multiplatformapplication.state.queryRequests
import com.example.multiplatformapplication.ui.components.ButtonStyle
import com.example.multiplatformapplication.ui.components.EmptyFeed
import com.example.multiplatformapplication.ui.components.JACpotButton
import com.example.multiplatformapplication.ui.components.RequestCard
import com.example.multiplatformapplication.ui.components.SearchBar
import kotlin.time.Clock

/** Searchable feed of student requests. One column under 600dp, an adaptive grid from 600dp up. */
@Composable
fun RequestsScreen(
    requests: List<Request>,
    onPostRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by rememberSaveable { mutableStateOf("") }
    // Read once so every card counts from the same moment while the screen is open.
    val now = remember { Clock.System.now().toEpochMilliseconds() }
    val active = queryRequests(requests)
    val visible = queryRequests(requests, query)

    BoxWithConstraints(modifier.fillMaxSize()) {
        val wide = maxWidth >= 600.dp
        LazyVerticalGrid(
            columns = if (wide) GridCells.Adaptive(300.dp) else GridCells.Fixed(1),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(if (wide) 24.dp else 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Requests", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.weight(1f))
                        JACpotButton("+ Post a request", onClick = onPostRequest)
                    }
                    SearchBar(
                        query = query,
                        onQueryChange = { query = it },
                        placeholder = "Search requests",
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "${visible.size} of ${active.size} requests",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        if (query.isNotBlank()) {
                            JACpotButton("Clear search", onClick = { query = "" }, style = ButtonStyle.Text)
                        }
                    }
                }
            }
            if (active.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    EmptyFeed(
                        "No requests yet",
                        "Looking for something? Post a request and other JAC students can answer it.",
                        "Post a request",
                        onPostRequest,
                    )
                }
            } else if (visible.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    EmptyFeed(
                        "No matching requests",
                        "Try another search, or post your own request.",
                        "Post a request",
                        onPostRequest,
                    )
                }
            }
            items(visible, key = { it.id }) { request ->
                RequestCard(request = request, now = now, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
