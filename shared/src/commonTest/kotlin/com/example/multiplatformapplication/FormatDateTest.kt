package com.example.multiplatformapplication

import com.example.multiplatformapplication.ui.screens.formatDate
import com.example.multiplatformapplication.ui.screens.toEpochMillis
import kotlin.test.Test
import kotlin.test.assertEquals

class FormatDateTest {
    @Test
    fun roundTripsWithToEpochMillis() {
        assertEquals("Jan 1, 1970", formatDate(0))
        assertEquals("Mar 5, 2027", formatDate(toEpochMillis(2027, 3, 5)))
        assertEquals("Feb 29, 2028", formatDate(toEpochMillis(2028, 2, 29)))
        assertEquals("Dec 31, 2026", formatDate(toEpochMillis(2026, 12, 31) + 86_399_000L))
    }
}
