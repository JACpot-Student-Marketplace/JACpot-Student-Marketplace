package com.example.multiplatformapplication.ui.screens

import kotlin.time.Clock

private const val DAY_MS = 86_400_000L

private fun isLeap(year: Int) = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)

/** Days in [month] (1-12). With no [year] yet, February allows 29 so a leap day can still be picked. */
internal fun daysInMonth(
    month: Int,
    year: Int?,
): Int =
    when (month) {
        2 -> if (year == null || isLeap(year)) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }

/** Current calendar year (UTC), from days since 1970 (civil-from-days algorithm). */
internal fun currentYear(): Int {
    val z =
        Clock.System
            .now()
            .toEpochMilliseconds()
            .floorDiv(DAY_MS) + 719_468
    val era = z.floorDiv(146_097)
    val doe = z - era * 146_097
    val yoe = (doe - doe / 1_460 + doe / 36_524 - doe / 146_096) / 365
    val doy = doe - (365 * yoe + yoe / 4 - yoe / 100)
    val month = (5 * doy + 2) / 153 // March-based month index
    return (yoe + era * 400 + if (month >= 10) 1 else 0).toInt()
}
