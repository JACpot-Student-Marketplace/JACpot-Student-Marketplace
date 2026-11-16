package com.example.multiplatformapplication.model

private const val MINUTE_MS = 60_000L
private const val HOUR_MS = 60 * MINUTE_MS
private const val DAY_MS = 24 * HOUR_MS

/** "Just now", "5 min ago", "3 h ago", "2 days ago". A time in the future counts as just now. */
fun formatTimeAgo(
    createdAt: Long,
    now: Long,
): String {
    val elapsed = now - createdAt
    return when {
        elapsed < MINUTE_MS -> "Just now"
        elapsed < HOUR_MS -> "${elapsed / MINUTE_MS} min ago"
        elapsed < DAY_MS -> "${elapsed / HOUR_MS} h ago"
        elapsed < 2 * DAY_MS -> "1 day ago"
        else -> "${elapsed / DAY_MS} days ago"
    }
}
