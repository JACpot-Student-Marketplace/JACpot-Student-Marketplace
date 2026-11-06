package com.example.multiplatformapplication.model

import kotlinx.serialization.Serializable

@Serializable
data class Request(
    val id: String,
    val ownerId: String,
    val title: String,
    val description: String,
    val courseCode: String? = null,
    val maxBudgetCents: Long? = null,
    val neededBy: Long? = null,
    val expiresAt: Long,
    val status: RequestStatus = RequestStatus.ACTIVE,
    val createdAt: Long,
    val updatedAt: Long,
)