package com.example.multiplatformapplication.ui.screens

import com.example.multiplatformapplication.model.ItemCondition
import com.example.multiplatformapplication.model.ListingCategory
import com.example.multiplatformapplication.model.label

private const val MAX_PHOTOS = 5
private val photoUrlFormat = Regex("""https?://\S+""", RegexOption.IGNORE_CASE)

internal fun parsePhotoUrls(text: String): List<String> = text.lines().map { it.trim() }.filter { it.isNotEmpty() }

internal data class ListingFormErrors(
    val title: String? = null,
    val description: String? = null,
    val price: String? = null,
    val category: String? = null,
    val condition: String? = null,
    val photos: String? = null,
) {
    val any get() = listOfNotNull(title, description, price, category, condition, photos).isNotEmpty()
}


internal fun validateListingForm(
    title: String,
    description: String,
    priceText: String,
    categoryLabel: String,
    conditionLabel: String,
    photosText: String,
): ListingFormErrors {
    val priceCents = parseBudgetCents(priceText)
    val photos = parsePhotoUrls(photosText)
    val categoryValid = ListingCategory.entries.any { it.label == categoryLabel }
    return ListingFormErrors(
        title = "Title is required.".takeIf { title.isBlank() },
        description = "Description is required.".takeIf { description.isBlank() },
        price =
            when {
                priceText.isBlank() -> "Price is required."
                priceCents == null -> "Enter an amount like 25 or 25.50."
                priceCents <= 0 -> "Price must be greater than zero."
                else -> null
            },
        category =
            when {
                categoryLabel.isBlank() -> "Pick a category."
                !categoryValid -> "Pick a valid category."
                else -> null
            },
        condition = "Pick a condition.".takeIf { ItemCondition.entries.none { it.label == conditionLabel } },
        photos =
            when {
                photos.size > MAX_PHOTOS -> "Add at most $MAX_PHOTOS photos."
                photos.any { !photoUrlFormat.matches(it) } -> "Each photo must be an http:// or https:// link."
                else -> null
            },
    )
}