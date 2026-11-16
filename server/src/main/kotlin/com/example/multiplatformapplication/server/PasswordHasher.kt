package com.example.multiplatformapplication.server

import at.favre.lib.crypto.bcrypt.BCrypt

class PasswordHasher(
    private val cost: Int = 12,
) {
    private val dummyHash: String = BCrypt.withDefaults().hashToString(cost, "dummy-password".toCharArray())

    fun hash(password: String): String = BCrypt.withDefaults().hashToString(cost, password.toCharArray())

    fun verify(
        password: String,
        hash: String,
    ): Boolean = BCrypt.verifyer().verify(password.toCharArray(), hash).verified

    /**
     * Unknown emails are checked against [dummyHash] so a missing account is not a fast rejection.
     * A null hash still fails the match.
     */
    fun matches(
        password: String,
        hash: String?,
    ): Boolean {
        val verified = runCatching { verify(password, hash ?: dummyHash) }.getOrDefault(false)
        return hash != null && verified
    }
}
