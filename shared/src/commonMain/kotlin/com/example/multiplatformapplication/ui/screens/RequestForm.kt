package com.example.multiplatformapplication.ui.screens

import com.example.multiplatformapplication.model.Request
import com.example.multiplatformapplication.model.requestExpiresAt

private val budgetFormat = Regex("""\d{1,9}(\.\d{1,2})?""")

/** "12", "12.5" or "12.50" as cents; null when empty or not a plain amount with at most two decimals. */
internal fun parseBudgetCents(text: String): Long? {
    val clean = text.trim()
    if (!budgetFormat.matches(clean)) return null
    val dollars = clean.substringBefore('.').toLong()
    val cents = clean.substringAfter('.', "").padEnd(2, '0').toLong()
    return dollars * 100 + cents
}

internal data class RequestFormErrors(
    val title: String? = null,
    val description: String? = null,
    val courseOrBudget: String? = null,
    val budget: String? = null,
    val neededBy: String? = null,
) {
    val any get() = listOfNotNull(title, description, courseOrBudget, budget, neededBy).isNotEmpty()
}

/** One message per field, null when the field is fine. The date is optional but all-or-nothing. */
internal fun validateRequestForm(
    title: String,
    description: String,
    courseCode: String,
    budgetText: String,
    month: Int?,
    day: Int?,
    year: Int?,
    nowMillis: Long,
): RequestFormErrors {
    val budgetCents = parseBudgetCents(budgetText)
    return RequestFormErrors(
        title = "Title is required.".takeIf { title.isBlank() },
        description = "Description is required.".takeIf { description.isBlank() },
        courseOrBudget = "Enter a course code or a maximum budget.".takeIf { courseCode.isBlank() && budgetText.isBlank() },
        budget =
            when {
                budgetText.isBlank() -> null
                budgetCents == null -> "Enter an amount like 25 or 25.50."
                budgetCents <= 0 -> "Budget must be greater than zero."
                else -> null
            },
        neededBy =
            when {
                listOf(month, day, year).all { it == null } -> null
                month == null || day == null || year == null -> "Pick a month, day and year, or leave all three empty."
                toEpochMillis(year, month, day) + DAY_MS <= nowMillis -> "Needed-by date can't be in the past."
                else -> null
            },
    )
}

/** Turns checked form input into a [Request]. Call only after [validateRequestForm] found no errors. */
internal fun buildRequest(
    title: String,
    description: String,
    courseCode: String,
    budgetText: String,
    month: Int?,
    day: Int?,
    year: Int?,
    nowMillis: Long,
): Request {
    val neededBy = if (month != null && day != null && year != null) toEpochMillis(year, month, day) else null
    return Request(
        id = "req-$nowMillis",
        ownerId = "me", // shortcut: no signed-in student yet, wire to the real user id with auth
        title = title.trim(),
        description = description.trim(),
        courseCode = courseCode.trim().ifBlank { null },
        maxBudgetCents = parseBudgetCents(budgetText),
        neededBy = neededBy,
        expiresAt = requestExpiresAt(nowMillis, neededBy),
        createdAt = nowMillis,
        updatedAt = nowMillis,
    )
}
