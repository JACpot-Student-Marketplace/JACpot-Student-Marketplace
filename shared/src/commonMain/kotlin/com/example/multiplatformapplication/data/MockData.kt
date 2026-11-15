package com.example.multiplatformapplication.data

import com.example.multiplatformapplication.model.ItemCondition
import com.example.multiplatformapplication.model.Listing
import com.example.multiplatformapplication.model.ListingCategory
import com.example.multiplatformapplication.model.ListingStatus
import com.example.multiplatformapplication.model.User

const val MOCK_CURRENT_USER_ID = "user-jaden"

private const val HOUR_MS = 3_600_000L

val mockUsers =
    listOf(
        User("user-jaden", "Jaden", "M.", "jaden@johnabbottcollege.net", createdAt = 0L),
        User("user-luca", "Luca", "M.", "luca@johnabbottcollege.net", createdAt = 0L),
        User("user-chris", "Chris", "C.", "chris@johnabbottcollege.net", createdAt = 0L),
        User("user-talib", "Talib", "A.", "talib@johnabbottcollege.net", createdAt = 0L),
    )

fun mockListings(now: Long): List<Listing> =
    listOf(
        mockListing(
            "mock-1",
            "user-luca",
            "Calculus Textbook",
            "Calculus: An Introduction, 8th edition.",
            ListingCategory.TEXTBOOKS,
            ItemCondition.GOOD,
            4000,
            hoursAgo = 2,
            now = now,
            courseCode = "201-MTH-01",
            author = "Great author",
            releaseDate = "2012-06-02",
            accessCodeUsed = true,
            photoUrls = listOf("https://m.media-amazon.com/images/I/8132ucjiueL._AC_UF1000,1000_QL80_.jpg"),
        ),
        mockListing(
            // Contains a broken url for testing stuff
            "mock-2",
            "user-chris",
            "Calculator",
            "scientific calculator",
            ListingCategory.SUPPLIES,
            ItemCondition.LIKE_NEW,
            1500,
            hoursAgo = 5,
            now = now,
            photoUrls = listOf("https://Invalid/broken.png"),
        ),
        mockListing(
            "mock-3",
            "user-talib",
            "Calculator",
            "Works perfectly, new batteries.",
            ListingCategory.SUPPLIES,
            ItemCondition.GOOD,
            1200,
            hoursAgo = 26,
            now = now,
        ),
        mockListing(
            "mock-4",
            "user-jaden",
            "Lab Coat",
            "White lab coat, size M. Worn for one semester of chemistry.",
            ListingCategory.CLOTHING,
            ItemCondition.LIKE_NEW,
            1800,
            hoursAgo = 30,
            now = now,
            courseCode = "308-SCI-05",
            photoUrls = listOf("https://campusstore.torontomu.ca/images/product/medium/24900.png"),
        ),
        mockListing(
            "mock-5",
            "user-luca",
            "Intro to Programming",
            "Full book",
            ListingCategory.TEXTBOOKS,
            ItemCondition.FAIR,
            2500,
            hoursAgo = 50,
            now = now,
            courseCode = "420-PG1-AB",
            author = "Larry",
            releaseDate = "2018-06-15",
            accessCodeUsed = false,
        ),
        mockListing(
            "mock-6",
            "user-chris",
            "JAC Campus Hoodie",
            "Navy John Abbott hoodie, size L.",
            ListingCategory.CLOTHING,
            ItemCondition.GOOD,
            2000,
            hoursAgo = 72,
            now = now,
            photoUrls = listOf("https://i.etsystatic.com/44927848/r/il/2bdecd/7237567270/il_1080xN.7237567270_p7bo.jpg"),
        ),
        mockListing(
            "mock-7",
            "user-jaden",
            "Desk Lamp",
            "LED desk lamp.",
            ListingCategory.OTHER,
            ItemCondition.GOOD,
            1000,
            hoursAgo = 96,
            now = now,
        ),
        mockListing(
            "mock-8",
            "user-talib",
            "Acoustic Guitar",
            "guitar with a soft case.",
            ListingCategory.HOBBY,
            ItemCondition.FAIR,
            8000,
            hoursAgo = 120,
            now = now,
            photoUrls = listOf("https://hub.yamaha.com/wp-content/uploads/2015/05/DSC00195-min-1-scaled.jpg"),
        ),
        mockListing(
            "mock-9",
            "user-luca",
            "Another calc",
            "calculator. Already sold, so it should never appear in the feed.",
            ListingCategory.ELECTRONICS,
            ItemCondition.GOOD,
            6000,
            hoursAgo = 140,
            now = now,
            status = ListingStatus.SOLD,
        ),
    )

private fun mockListing(
    id: String,
    ownerId: String,
    title: String,
    description: String,
    category: ListingCategory,
    condition: ItemCondition,
    priceCents: Long,
    hoursAgo: Long,
    now: Long,
    courseCode: String? = null,
    author: String? = null,
    releaseDate: String? = null,
    accessCodeUsed: Boolean? = null,
    photoUrls: List<String> = emptyList(),
    status: ListingStatus = ListingStatus.ACTIVE,
): Listing {
    val createdAt = now - hoursAgo * HOUR_MS
    return Listing(
        id = id,
        ownerId = ownerId,
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
        status = status,
        createdAt = createdAt,
        updatedAt = createdAt,
    )
}
