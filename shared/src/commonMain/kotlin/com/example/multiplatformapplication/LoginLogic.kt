package com.example.multiplatformapplication

data class User(
    val username: String,
    val password: String,
)

val users =
    listOf(
        User("Luca", "Luca123"),
        User("Jaden", "Jaden123"),
        User("Chris", "Chris123"),
        User("Talib", "Talib123"),
    )

fun authenticate(
    username: String,
    password: String,
): Boolean {
    val match = users.find { it.username == username } ?: return false
    return match.password == password
}

fun formatAttempt(
    username: String,
    wasSuccessful: Boolean,
): String = "$username - ${if (wasSuccessful) "success" else "failed"}"
