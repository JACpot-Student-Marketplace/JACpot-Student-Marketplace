package com.example.multiplatformapplication.server

import com.example.multiplatformapplication.model.Listing
import com.example.multiplatformapplication.model.ListingStatus
import com.example.multiplatformapplication.model.NewListing
import com.example.multiplatformapplication.model.User
import java.util.UUID

class InMemoryStudentStore : StudentStore {
    val students = mutableListOf<StoredStudent>()
    val sessions = mutableListOf<StoredSession>()
    private val listings = mutableListOf<Listing>()
    private val lock = Any()

    override suspend fun users(): List<User> = synchronized(lock) { students.map { it.user } }

    override suspend fun listings(): List<Listing> = synchronized(lock) { listings.toList() }

    override suspend fun createListing(listing: NewListing): Listing {
        val now = System.currentTimeMillis()
        val created =
            Listing(
                id = UUID.randomUUID().toString(),
                ownerId = listing.ownerId,
                title = listing.title,
                description = listing.description,
                category = listing.category,
                condition = listing.condition,
                priceCents = listing.priceCents,
                courseCode = listing.courseCode,
                author = listing.author,
                releaseDate = listing.releaseDate,
                accessCodeUsed = listing.accessCodeUsed,
                status = ListingStatus.ACTIVE,
                createdAt = now,
                updatedAt = now,
            )
        synchronized(lock) { listings += created }
        return created
    }

    override suspend fun findByEmail(email: String): StoredStudent? = synchronized(lock) { students.find { it.user.email == email } }

    override suspend fun insertStudent(
        firstName: String,
        lastName: String,
        email: String,
        passwordHash: String,
    ): User =
        synchronized(lock) {
            if (students.any { it.user.email == email }) throw DuplicateEmailException()
            val user =
                User(
                    id = UUID.randomUUID().toString(),
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    createdAt = System.currentTimeMillis(),
                )
            students += StoredStudent(user, passwordHash)
            user
        }

    override suspend fun saveSession(
        tokenHash: String,
        userId: String,
        expiresAt: Long,
    ) {
        synchronized(lock) {
            sessions += StoredSession(tokenHash = tokenHash, userId = userId, expiresAt = expiresAt)
        }
    }

    override suspend fun findValidSession(
        tokenHash: String,
        nowMillis: Long,
    ): StudentPrincipal? =
        synchronized(lock) {
            val session = sessions.find { it.tokenHash == tokenHash } ?: return null
            if (session.expiresAt <= nowMillis) return null
            val student = students.find { it.user.id == session.userId } ?: return null
            StudentPrincipal(userId = student.user.id, user = student.user)
        }

    override suspend fun updatePassword(
        userId: String,
        passwordHash: String,
    ) {
        synchronized(lock) {
            val index = students.indexOfFirst { it.user.id == userId }
            if (index >= 0) students[index] = students[index].copy(passwordHash = passwordHash)
        }
    }

    override suspend fun deleteSession(tokenHash: String) {
        synchronized(lock) {
            sessions.removeAll { it.tokenHash == tokenHash }
        }
    }
}

data class StoredSession(
    val tokenHash: String,
    val userId: String,
    val expiresAt: Long,
)
