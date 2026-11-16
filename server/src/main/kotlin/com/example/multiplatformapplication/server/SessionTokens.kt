package com.example.multiplatformapplication.server

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

const val SESSION_TTL_MILLIS: Long = 14L * 24 * 60 * 60 * 1000

private val secureRandom = SecureRandom()

fun newSessionToken(): String {
    val bytes = ByteArray(32)
    secureRandom.nextBytes(bytes)
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
}

fun hashSessionToken(token: String): String {
    val digest = MessageDigest.getInstance("SHA-256").digest(token.toByteArray(Charsets.UTF_8))
    return buildString(digest.size * 2) {
        for (byte in digest) {
            append("%02x".format(byte.toInt() and 0xff))
        }
    }
}
