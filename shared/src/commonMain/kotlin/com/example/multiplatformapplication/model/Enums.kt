package com.example.multiplatformapplication.model

import kotlinx.serialization.Serializable

@Serializable
enum class ListingCategory { TEXTBOOKS, SUPPLIES, CLOTHING, ELECTRONICS, HOBBY, OTHER }

@Serializable
enum class ItemCondition { NEW, LIKE_NEW, GOOD, FAIR, POOR }

@Serializable
enum class ListingStatus { ACTIVE, SOLD, DELETED }

@Serializable
enum class RequestStatus { ACTIVE, CLOSED, FULFILLED, EXPIRED }

@Serializable
enum class OfferStatus { PENDING, ACCEPTED, DECLINED }

@Serializable
enum class MeetupZone { LIBRARY_ENTRANCE, CASGRAIN, HERZBERG, PENFIELD }