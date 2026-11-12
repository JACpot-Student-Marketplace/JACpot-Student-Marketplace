package com.example.multiplatformapplication

import com.example.multiplatformapplication.ui.screens.daysInMonth
import com.example.multiplatformapplication.ui.screens.parseBudgetCents
import com.example.multiplatformapplication.ui.screens.toEpochMillis
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RequestInputTest {
    @Test
    fun budgetBecomesCents() {
        assertEquals(1200L, parseBudgetCents("12"))
        assertEquals(1250L, parseBudgetCents("12.5"))
        assertEquals(1250L, parseBudgetCents(" 12.50 "))
        assertEquals(99L, parseBudgetCents("0.99"))
    }

    @Test
    fun badBudgetIsNull() {
        listOf("", "abc", "12.345", "-5", "1,5", "$5", ".5").forEach { assertNull(parseBudgetCents(it), it) }
    }

    @Test
    fun datesBecomeEpochMillis() {
        assertEquals(0L, toEpochMillis(1970, 1, 1))
        assertEquals(951_782_400_000L, toEpochMillis(2000, 2, 29))
        assertEquals(1_794_182_400_000L, toEpochMillis(2026, 11, 9))
    }

    @Test
    fun februaryFollowsLeapYears() {
        assertEquals(29, daysInMonth(2, 2024))
        assertEquals(28, daysInMonth(2, 2023))
        assertEquals(28, daysInMonth(2, 2100))
        assertEquals(29, daysInMonth(2, null))
        assertEquals(30, daysInMonth(4, 2026))
    }
}
