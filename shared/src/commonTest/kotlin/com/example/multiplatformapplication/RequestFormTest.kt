package com.example.multiplatformapplication

import com.example.multiplatformapplication.model.validateRequest
import com.example.multiplatformapplication.ui.screens.buildRequest
import com.example.multiplatformapplication.ui.screens.daysInMonth
import com.example.multiplatformapplication.ui.screens.parseBudgetCents
import com.example.multiplatformapplication.ui.screens.toEpochMillis
import com.example.multiplatformapplication.ui.screens.validateRequestForm
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RequestFormTest {
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

    private val today = toEpochMillis(2026, 11, 10)

    private fun check(
        title: String = "Calculus book",
        description: String = "Need it for MAT 101",
        course: String = "201-NYA",
        budget: String = "",
        month: Int? = null,
        day: Int? = null,
        year: Int? = null,
    ) = validateRequestForm(title, description, course, budget, month, day, year, today)

    @Test
    fun validRequestHasNoErrors() {
        assertFalse(check().any)
        assertFalse(check(course = "", budget = "20").any)
        assertFalse(check(budget = "20", month = 12, day = 1, year = 2026).any)
        assertFalse(check(month = 11, day = 10, year = 2026).any) // today is still fine
    }

    @Test
    fun titleAndDescriptionAreRequired() {
        assertEquals("Title is required.", check(title = " ").title)
        assertEquals("Description is required.", check(description = "").description)
    }

    @Test
    fun needsCourseCodeOrBudget() {
        assertTrue(check(course = "", budget = "").courseOrBudget != null)
        assertNull(check(course = "", budget = "5").courseOrBudget)
    }

    @Test
    fun budgetMustBePositiveAmount() {
        assertTrue(check(budget = "0").budget != null)
        assertTrue(check(budget = "abc").budget != null)
    }

    @Test
    fun dateIsAllOrNothingAndNotInThePast() {
        assertTrue(check(month = 12).neededBy != null)
        assertTrue(check(month = 12, day = 1).neededBy != null)
        assertTrue(check(month = 11, day = 9, year = 2026).neededBy != null)
    }

    @Test
    fun builtRequestIsCleanAndPassesTheModelRules() {
        val r = buildRequest("  Calculus  ", " book ", " ", "20.5", null, null, null, today)
        assertEquals("Calculus", r.title)
        assertEquals("book", r.description)
        assertNull(r.courseCode)
        assertEquals(2050L, r.maxBudgetCents)
        assertNull(r.neededBy)
        assertEquals(today + 30L * 24 * 60 * 60 * 1000, r.expiresAt)
        assertEquals(emptyList(), validateRequest(r))
    }

    @Test
    fun neededBySoonerThanThirtyDaysSetsExpiry() {
        val r = buildRequest("t", "d", "201-NYA", "", 11, 20, 2026, today)
        assertEquals(toEpochMillis(2026, 11, 20), r.neededBy)
        assertEquals(r.neededBy, r.expiresAt)
    }
}
