package com.example.multiplatformapplication.model

val ListingCategory.label: String
    get() =
        when (this) {
            ListingCategory.TEXTBOOKS -> "Textbooks"
            ListingCategory.SUPPLIES -> "School supplies"
            ListingCategory.CLOTHING -> "Clothing"
            ListingCategory.ELECTRONICS -> "Electronics"
            ListingCategory.HOBBY -> "Hobby"
            ListingCategory.OTHER -> "Other"
        }

val ItemCondition.label: String
    get() =
        when (this) {
            ItemCondition.NEW -> "New"
            ItemCondition.LIKE_NEW -> "Like new"
            ItemCondition.GOOD -> "Good"
            ItemCondition.FAIR -> "Fair"
            ItemCondition.POOR -> "Poor"
        }

// In cad prices from cents, so 100 is $1. 1250 would be $12.50
fun formatPrice(cents: Long): String {
    val dollars = cents / 100
    val rest = cents % 100
    return if (rest == 0L) "\$$dollars" else "\$$dollars.${rest.toString().padStart(2, '0')}"
}
