package com.example.multiplatformapplication.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
)

@Serializable
data class AuthResponse(
    val token: String,
    val expiresAt: Long,
    val user: User,
)

@Serializable
data class ApiError(
    val error: String,
)
