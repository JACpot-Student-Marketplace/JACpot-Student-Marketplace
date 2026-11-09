package com.example.multiplatformapplication.model

private const val JAC_EMAIL_SUFFIX = "@johnabbottcollege.net"
private const val THIRTY_DAYS_MS = 30L * 24 * 60 * 60 * 1000

fun validateEmail(email: String): String? =
    if (email == email.lowercase() && email.endsWith(JAC_EMAIL_SUFFIX) && email.substringBefore("@").isNotBlank()) {
        null
    } else {
        "Use a lowercase @johnabbottcollege.net email."
    }

fun validateListing(listing: Listing): List<String> {
    val errors = mutableListOf<String>()
    if (listing.title.isBlank()) errors += "Title is required."
    if (listing.description.isBlank()) errors += "Description is required."
    if (listing.priceCents <= 0) errors += "Price must be greater than zero."
    if (listing.photoUrls.size > 5) errors += "A listing can have at most five photos."
    val hasBookDetails = listing.author != null || listing.releaseDate != null || listing.accessCodeUsed != null
    if (listing.category != ListingCategory.TEXTBOOKS && hasBookDetails) {
        errors += "Author, release date, and access code apply only to textbooks."
    }
    return errors
}

fun validateRequest(request: Request): List<String> {
    val errors = mutableListOf<String>()
    if (request.title.isBlank()) errors += "Title is required."
    if (request.description.isBlank()) errors += "Description is required."
    val hasCourse = !request.courseCode.isNullOrBlank()
    val hasBudget = request.maxBudgetCents != null
    if (!hasCourse && !hasBudget) errors += "Provide a course code or a maximum budget."
    if (request.maxBudgetCents != null && request.maxBudgetCents <= 0) {
        errors += "Budget must be greater than zero."
    }
    return errors
}

fun requestExpiresAt(
    createdAt: Long,
    neededBy: Long?,
): Long {
    val afterThirtyDays = createdAt + THIRTY_DAYS_MS
    return if (neededBy != null && neededBy < afterThirtyDays) neededBy else afterThirtyDays
}

fun validateConversation(conversation: Conversation): List<String> {
    val errors = mutableListOf<String>()
    val hasListing = conversation.listingId != null
    val hasRequest = conversation.requestId != null
    if (hasListing == hasRequest) errors += "A conversation belongs to one listing or one request."
    if (conversation.buyerId == conversation.sellerId) errors += "Buyer and seller must be different students."
    return errors
}

fun validateMessage(message: Message): String? = if (message.body.isBlank()) "Message text is required." else null

fun validateBuyRequest(buyRequest: BuyRequest): String? = if (buyRequest.priceCents < 0) "Offer price must be zero or greater." else null
