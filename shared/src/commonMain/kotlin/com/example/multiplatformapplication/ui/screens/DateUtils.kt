package com.example.multiplatformapplication.ui.screens

import kotlin.time.Clock

internal const val DAY_MS = 86_400_000L

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

/** Midnight UTC of the given date as epoch millis (days-from-civil algorithm). Caller has checked the date is real. */
internal fun toEpochMillis(
    year: Int,
    month: Int,
    day: Int,
): Long {
    val y = (if (month <= 2) year - 1 else year).toLong()
    val era = y.floorDiv(400)
    val yoe = y - era * 400
    val doy = (153 * (month + (if (month > 2) -3 else 9)) + 2) / 5 + day - 1
    val doe = yoe * 365 + yoe / 4 - yoe / 100 + doy
    return (era * 146_097 + doe - 719_468) * DAY_MS
}
