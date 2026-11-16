package com.example.multiplatformapplication.state

import com.example.multiplatformapplication.model.Request
import com.example.multiplatformapplication.model.RequestStatus

/** Active requests whose title, description or course code contain [search], newest first. */
fun queryRequests(
    requests: List<Request>,
    search: String = "",
): List<Request> {
    val term = search.trim()
    return requests
        .filter { request ->
            request.status == RequestStatus.ACTIVE &&
                (
                    term.isEmpty() ||
                        request.title.contains(term, ignoreCase = true) ||
                        request.description.contains(term, ignoreCase = true) ||
                        request.courseCode?.contains(term, ignoreCase = true) == true
                )
        }.sortedByDescending { it.createdAt }
}
