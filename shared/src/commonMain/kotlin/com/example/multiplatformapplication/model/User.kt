package com.example.multiplatformapplication.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val profilePhotoUrl: String? = null,
    val createdAt: Long,
)