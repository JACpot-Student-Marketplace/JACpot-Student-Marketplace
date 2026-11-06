package com.example.multiplatformapplication.model

import kotlinx.serialization.Serializable

@Serializable
data class Listing(
    val id: String,
    val ownerId: String,
    val title: String,
    val description: String,
    val category: ListingCategory,
    val condition: ItemCondition,
    val priceCents: Long,
    val courseCode: String? = null,
    val author: String? = null,
    val releaseDate: String? = null,
    val accessCodeUsed: Boolean? = null,
    val photoUrls: List<String> = emptyList(),
    val status: ListingStatus = ListingStatus.ACTIVE,
    val createdAt: Long,
    val updatedAt: Long,
)