package com.example.multiplatformapplication.auth

data class DemoAccount(
    val email: String,
    val password: String,
)

val demoAccounts =
    listOf(
        User("luca@johnabbottcollege.net", "Luca123"),
        User("jaden@johnabbottcollege.net", "Jaden123"),
        User("chris@johnabbottcollege.net", "Chris123"),
        User("talib@johnabbottcollege.net", "Talib123"),
    )

fun authenticate(
    email: String,
    password: String,
): Boolean {
    val match = demoAccounts.find { it.username == username } ?: return false
    return match.password == password
}