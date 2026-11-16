package com.example.multiplatformapplication.server

import com.example.multiplatformapplication.model.Listing
import com.example.multiplatformapplication.model.NewListing
import com.example.multiplatformapplication.model.User

data class StoredStudent(
    val user: User,
    val passwordHash: String?,
)

data class StudentPrincipal(
    val userId: String,
    val user: User,
)

class DuplicateEmailException : RuntimeException()

interface StudentStore {
    suspend fun users(): List<User>

    suspend fun listings(): List<Listing>

    suspend fun createListing(listing: NewListing): Listing

    suspend fun findByEmail(email: String): StoredStudent?

    suspend fun insertStudent(
        firstName: String,
        lastName: String,
        email: String,
        passwordHash: String,
    ): User

    suspend fun saveSession(
        tokenHash: String,
        userId: String,
        expiresAt: Long,
    )

    suspend fun findValidSession(
        tokenHash: String,
        nowMillis: Long,
    ): StudentPrincipal?

    suspend fun updatePassword(
        userId: String,
        passwordHash: String,
    )

    suspend fun deleteSession(tokenHash: String)
}
