package com.example.multiplatformapplication

import com.example.multiplatformapplication.data.mockListings
import com.example.multiplatformapplication.model.ItemCondition
import com.example.multiplatformapplication.model.ListingCategory
import com.example.multiplatformapplication.model.ListingStatus
import com.example.multiplatformapplication.state.ListingsViewModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ListingsViewModelTest {
    private val fixedNow = 1_000_000L

    @Test
    fun testInitialListingsAndActiveFilter() {
        val seed = mockListings(fixedNow)
        val viewModel = ListingsViewModel(currentUserId = "user-jaden", seed = seed)

        // Seed has 9 total listings, but mock-9 is SOLD, so activeListings should exclude it.
        assertEquals(9, viewModel.listings.size)

        val active = viewModel.activeListings
        // Verify that the sold listing (mock-9) is filtered out
        assertFalse(active.any { it.status == ListingStatus.SOLD })
        // Verify they are sorted by createdAt descending
        for (i in 0 until active.size - 1) {
            assertTrue(active[i].createdAt >= active[i + 1].createdAt)
        }
    }

    @Test
    fun testFindListing() {
        val viewModel = ListingsViewModel(seed = mockListings(fixedNow))

        val found = viewModel.findListing("mock-1")
        assertNotNull(found)
        assertEquals("Calculus Textbook", found.title)

        val notFound = viewModel.findListing("non-existent-id")
        assertNull(notFound)
    }

    @Test
    fun testOwnershipAndSeller() {
        val seed = mockListings(fixedNow)
        val viewModel = ListingsViewModel(currentUserId = "user-jaden", seed = seed)

        val jadenListing = viewModel.findListing("mock-4") // owned by user-jaden
        val lucaListing = viewModel.findListing("mock-1") // owned by user-luca

        assertNotNull(jadenListing)
        assertTrue(viewModel.isOwner(jadenListing))
        val sellerJaden = viewModel.seller(jadenListing)
        assertNotNull(sellerJaden)
        assertEquals("Jaden", sellerJaden.firstName)

        assertNotNull(lucaListing)
        assertFalse(viewModel.isOwner(lucaListing))
        val sellerLuca = viewModel.seller(lucaListing)
        assertNotNull(sellerLuca)
        assertEquals("Luca", sellerLuca.firstName)
    }

    @Test
    fun testAddListing() {
        val viewModel = ListingsViewModel(currentUserId = "user-jaden", seed = emptyList())
        assertEquals(0, viewModel.listings.size)

        val newListing =
            viewModel.addListing(
                title = "TI-84 Calculator",
                description = "Graphing calculator in great shape.",
                category = ListingCategory.SUPPLIES,
                condition = ItemCondition.GOOD,
                priceCents = 5000,
                courseCode = "201-NYA-05",
            )

        assertEquals(1, viewModel.listings.size)
        assertEquals("local-1", newListing.id)
        assertEquals("user-jaden", newListing.ownerId)
        assertEquals("TI-84 Calculator", newListing.title)
        assertEquals(ListingStatus.ACTIVE, newListing.status)

        // Ensure activeListings reflects the addition
        assertEquals(1, viewModel.activeListings.size)
    }

    @Test
    fun testMarkSold() {
        val seed = mockListings(fixedNow)
        val viewModel = ListingsViewModel(currentUserId = "user-jaden", seed = seed)

        // mock-4 is owned by "user-jaden"
        val listingId = "mock-4"
        assertEquals(ListingStatus.ACTIVE, viewModel.findListing(listingId)?.status)

        viewModel.markSold(listingId)

        assertEquals(ListingStatus.SOLD, viewModel.findListing(listingId)?.status)
        // Should no longer appear in active listings
        assertFalse(viewModel.activeListings.any { it.id == listingId })
    }

    @Test
    fun testDeleteListingOnlyByOwner() {
        val seed = mockListings(fixedNow)
        // Current user is "user-jaden"
        val viewModel = ListingsViewModel(currentUserId = "user-jaden", seed = seed)

        // Try to delete "mock-1" which belongs to "user-luca" -> should fail/do nothing
        viewModel.deleteListing("mock-1")
        assertEquals(ListingStatus.ACTIVE, viewModel.findListing("mock-1")?.status)

        // Delete "mock-4" which belongs to "user-jaden" -> should succeed
        viewModel.deleteListing("mock-4")
        assertEquals(ListingStatus.DELETED, viewModel.findListing("mock-4")?.status)
    }
}
