package com.example.multiplatformapplication

import com.example.multiplatformapplication.model.formatTimeAgo
import kotlin.test.Test
import kotlin.test.assertEquals

class TimeAgoTest {
    private val now = 1_800_000_000_000L
    private val minute = 60_000L
    private val hour = 60 * minute
    private val day = 24 * hour

    private fun ago(ms: Long) = formatTimeAgo(now - ms, now)

    @Test
    fun recentIsJustNow() {
        assertEquals("Just now", ago(30_000))
        assertEquals("Just now", ago(-5 * minute)) // clock skew: a time in the future
    }

    @Test
    fun minutesHoursDays() {
        assertEquals("5 min ago", ago(5 * minute))
        assertEquals("59 min ago", ago(59 * minute + 59_000))
        assertEquals("1 h ago", ago(hour))
        assertEquals("23 h ago", ago(23 * hour + 59 * minute))
        assertEquals("1 day ago", ago(day + 5 * hour))
        assertEquals("3 days ago", ago(3 * day))
    }
}
