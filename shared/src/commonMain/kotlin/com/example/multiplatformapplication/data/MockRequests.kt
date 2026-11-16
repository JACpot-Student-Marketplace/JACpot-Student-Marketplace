package com.example.multiplatformapplication.data

import com.example.multiplatformapplication.model.Request
import com.example.multiplatformapplication.model.requestExpiresAt

private const val HOUR_MS = 3_600_000L
private const val DAY_MS = 24 * HOUR_MS

/** Sample requests for the feed: a mix of course code only, budget only and both, with and without a needed-by date. */
fun mockRequests(now: Long): List<Request> =
    listOf(
        mockRequest(
            "mock-req-1",
            "user-chris",
            "Calculus I textbook",
            "Looking for the course textbook, any recent edition is fine.",
            hoursAgo = 3,
            now = now,
            courseCode = "201-NYA-05",
            budgetCents = 4000,
        ),
        mockRequest(
            "mock-req-2",
            "user-talib",
            "Scientific calculator",
            "Need one before my exam, doesn't have to be fancy.",
            hoursAgo = 7,
            now = now,
            budgetCents = 2000,
            neededInDays = 6,
        ),
        mockRequest(
            "mock-req-3",
            "user-luca",
            "Intro to Programming notes",
            "Would love a copy of the printed course notes if someone is done with them.",
            hoursAgo = 28,
            now = now,
            courseCode = "420-110-DW",
        ),
        mockRequest(
            "mock-req-4",
            "user-jaden",
            "TI-84 graphing calculator",
            "Borrowing or buying, either works. Needed for the statistics unit.",
            hoursAgo = 52,
            now = now,
            courseCode = "201-FSC-03",
            budgetCents = 6000,
            neededInDays = 12,
        ),
        mockRequest(
            "mock-req-5",
            "user-talib",
            "Winter jacket, size M",
            "Mostly worn is fine as long as the zipper works.",
            hoursAgo = 80,
            now = now,
            budgetCents = 3500,
        ),
        mockRequest(
            "mock-req-6",
            "user-chris",
            "Psychology textbook",
            "Introduction to Psychology, access code not needed.",
            hoursAgo = 120,
            now = now,
            courseCode = "350-102-MQ",
            budgetCents = 2500,
        ),
    )

private fun mockRequest(
    id: String,
    ownerId: String,
    title: String,
    description: String,
    hoursAgo: Int,
    now: Long,
    courseCode: String? = null,
    budgetCents: Long? = null,
    neededInDays: Int? = null,
): Request {
    val createdAt = now - hoursAgo * HOUR_MS
    val neededBy = neededInDays?.let { now + it * DAY_MS }
    return Request(
        id = id,
        ownerId = ownerId,
        title = title,
        description = description,
        courseCode = courseCode,
        maxBudgetCents = budgetCents,
        neededBy = neededBy,
        expiresAt = requestExpiresAt(createdAt, neededBy),
        createdAt = createdAt,
        updatedAt = createdAt,
    )
}
