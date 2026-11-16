package com.example.multiplatformapplication.auth

data class DemoAccount(
    val email: String,
    val password: String,
)

val demoAccounts =
    listOf(
        DemoAccount("luca@johnabbottcollege.net", "Luca123"),
        DemoAccount("jaden@johnabbottcollege.net", "Jaden123"),
        DemoAccount("chris@johnabbottcollege.net", "Chris123"),
        DemoAccount("talib@johnabbottcollege.net", "Talib123"),
    )

fun authenticate(
    email: String,
    password: String,
): Boolean {
    val match = demoAccounts.find { it.email == email } ?: return false
    return match.password == password
}