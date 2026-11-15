package com.example.multiplatformapplication.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.multiplatformapplication.data.MOCK_CURRENT_USER_ID
import com.example.multiplatformapplication.data.mockListings
import com.example.multiplatformapplication.data.mockUsers
import com.example.multiplatformapplication.model.ItemCondition
import com.example.multiplatformapplication.model.Listing
import com.example.multiplatformapplication.model.ListingCategory
import com.example.multiplatformapplication.model.ListingStatus
import com.example.multiplatformapplication.model.User
import kotlin.time.Clock

class ListingsViewModel(
    val currentUserId: String = MOCK_CURRENT_USER_ID,
    seed: List<Listing> = mockListings(now()),
) : ViewModel() {
    var listings: List<Listing> by mutableStateOf(seed)
        private set

    private var nextId = 1

    val activeListings: List<Listing>
        get() = listings.filter { it.status == ListingStatus.ACTIVE }.sortedByDescending { it.createdAt }

    fun findListing(id: String): Listing? = listings.firstOrNull { it.id == id }

    fun seller(listing: Listing): User? = mockUsers.firstOrNull { it.id == listing.ownerId }

    fun isOwner(listing: Listing): Boolean = listing.ownerId == currentUserId

    fun addListing(
        title: String,
        description: String,
        category: ListingCategory,
        condition: ItemCondition,
        priceCents: Long,
        courseCode: String? = null,
        author: String? = null,
        releaseDate: String? = null,
        accessCodeUsed: Boolean? = null,
        photoUrls: List<String> = emptyList(),
    ): Listing {
        val now = now()
        val listing =
            Listing(
                id = "local-${nextId++}",
                ownerId = currentUserId,
                title = title,
                description = description,
                category = category,
                condition = condition,
                priceCents = priceCents,
                courseCode = courseCode,
                author = author,
                releaseDate = releaseDate,
                accessCodeUsed = accessCodeUsed,
                photoUrls = photoUrls,
                createdAt = now,
                updatedAt = now,
            )
        listings = listings + listing
        return listing
    }

    fun markSold(id: String) = setStatus(id, ListingStatus.SOLD)

    fun deleteListing(id: String) = setStatus(id, ListingStatus.DELETED)

    private fun setStatus(
        id: String,
        status: ListingStatus,
    ) {
        val now = now()
        listings =
            listings.map {
                if (it.id == id && it.ownerId == currentUserId) it.copy(status = status, updatedAt = now) else it
            }
    }
}

private fun now(): Long = Clock.System.now().toEpochMilliseconds()
