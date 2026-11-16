package com.example.multiplatformapplication

import com.example.multiplatformapplication.data.mockRequests
import com.example.multiplatformapplication.model.RequestStatus
import com.example.multiplatformapplication.state.queryRequests
import kotlin.test.Test
import kotlin.test.assertEquals

class RequestQueryTest {
    private val all = mockRequests(1_800_000_000_000L)

    private fun titles(search: String) = queryRequests(all, search).map { it.title }

    @Test
    fun blankSearchReturnsEveryActiveRequestNewestFirst() {
        val result = queryRequests(all, "  ")
        assertEquals(all.size, result.size)
        assertEquals(result.sortedByDescending { it.createdAt }, result)
    }

    @Test
    fun searchesTitleDescriptionAndCourseCodeIgnoringCase() {
        assertEquals(listOf("Winter jacket, size M"), titles("JACKET"))
        assertEquals(listOf("Intro to Programming notes"), titles("420-110"))
        assertEquals(listOf("TI-84 graphing calculator"), titles("statistics unit")) // description only
    }

    @Test
    fun noMatchGivesEmptyList() {
        assertEquals(emptyList(), titles("zebra"))
    }

    @Test
    fun closedRequestsAreHidden() {
        val closed = all.first().copy(status = RequestStatus.CLOSED)
        assertEquals(all.size - 1, queryRequests(listOf(closed) + all.drop(1)).size)
    }
}
