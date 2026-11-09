package com.example.multiplatformapplication.model

import kotlinx.serialization.Serializable

@Serializable
data class BuyRequest(
    val id: String,
    val conversationId: String,
    val buyerId: String,
    val priceCents: Long,
    val meetupZone: MeetupZone,
    val status: OfferStatus = OfferStatus.PENDING,
    val createdAt: Long,
    val decidedAt: Long? = null,
)