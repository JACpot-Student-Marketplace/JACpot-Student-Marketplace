package com.example.multiplatformapplication

import com.example.multiplatformapplication.data.mockRequests
import com.example.multiplatformapplication.model.validateRequest
import kotlin.test.Test
import kotlin.test.assertEquals

class MockRequestsTest {
    private val now = 1_800_000_000_000L

    @Test
    fun everySampleRequestPassesTheModelRules() {
        mockRequests(now).forEach { assertEquals(emptyList(), validateRequest(it), it.title) }
    }

    @Test
    fun idsAreUnique() {
        val ids = mockRequests(now).map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }
}
