package com.example.multiplatformapplication.model

import kotlinx.serialization.Serializable

@Serializable
data class Conversation(
    val id: String,
    val listingId: String? = null,
    val requestId: String? = null,
    val buyerId: String,
    val sellerId: String,
    val createdAt: Long,
    val lastMessageAt: Long,
)
