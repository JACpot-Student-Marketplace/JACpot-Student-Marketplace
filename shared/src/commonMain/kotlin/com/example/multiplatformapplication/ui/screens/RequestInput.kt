package com.example.multiplatformapplication.ui.screens

private val budgetFormat = Regex("""\d{1,9}(\.\d{1,2})?""")

/** "12", "12.5" or "12.50" as cents; null when empty or not a plain amount with at most two decimals. */
internal fun parseBudgetCents(text: String): Long? {
    val clean = text.trim()
    if (!budgetFormat.matches(clean)) return null
    val dollars = clean.substringBefore('.').toLong()
    val cents = clean.substringAfter('.', "").padEnd(2, '0').toLong()
    return dollars * 100 + cents
}
