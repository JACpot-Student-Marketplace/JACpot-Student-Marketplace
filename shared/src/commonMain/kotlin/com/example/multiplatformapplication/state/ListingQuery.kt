package com.example.multiplatformapplication.state

import com.example.multiplatformapplication.model.ItemCondition
import com.example.multiplatformapplication.model.Listing
import com.example.multiplatformapplication.model.ListingCategory

enum class ListingSort(
    val label: String,
) {
    NEWEST("Newest"),
    PRICE_LOW("Price: low to high"),
    PRICE_HIGH("Price: high to low"),
}

fun queryListings(
    listings: List<Listing>,
    search: String = "",
    category: ListingCategory? = null,
    condition: ItemCondition? = null,
    sort: ListingSort = ListingSort.NEWEST,
): List<Listing> {
    val term = search.trim()
    val filtered =
        listings.filter { listing ->
            val matchesSearch =
                term.isEmpty() ||
                    listing.title.contains(term, ignoreCase = true) ||
                    listing.description.contains(term, ignoreCase = true) ||
                    listing.courseCode?.contains(term, ignoreCase = true) == true
            matchesSearch &&
                (category == null || listing.category == category) &&
                (condition == null || listing.condition == condition)
        }
    val comparator =
        when (sort) {
            ListingSort.NEWEST -> compareByDescending<Listing> { it.createdAt }
            ListingSort.PRICE_LOW -> compareBy<Listing> { it.priceCents }
            ListingSort.PRICE_HIGH -> compareByDescending<Listing> { it.priceCents }
        }.thenByDescending { it.createdAt }
    return filtered.sortedWith(comparator)
}
