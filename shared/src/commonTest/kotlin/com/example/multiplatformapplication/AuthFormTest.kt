package com.example.multiplatformapplication

import com.example.multiplatformapplication.auth.authenticate
import com.example.multiplatformapplication.ui.screens.validateCreateAccount
import com.example.multiplatformapplication.ui.screens.validateLogin
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AuthFormTest {
    @Test
    fun validAccountCreationHasNoErrors() {
        val errors =
            validateCreateAccount(
                name = "Luca",
                email = "luca@johnabbottcollege.net",
                password = "Luca123",
            )

        assertFalse(errors.any)
        assertNull(errors.name)
        assertNull(errors.email)
        assertNull(errors.password)
    }

    @Test
    fun trimmedAbbottEmailIsAccepted() {
        val errors =
            validateCreateAccount(
                name = "Luca",
                email = "  luca@johnabbottcollege.net  ",
                password = "Luca123",
            )

        assertFalse(errors.any)
    }

    @Test
    fun createAccountRequiresNameEmailAndPassword() {
        val errors = validateCreateAccount(name = " ", email = " ", password = " ")

        assertTrue(errors.any)
        assertEquals("Name is required.", errors.name)
        assertEquals("Email is required.", errors.email)
        assertEquals("Password is required.", errors.password)
    }

    @Test
    fun eachCreateAccountFieldIsRequiredOnItsOwn() {
        assertEquals(
            "Name is required.",
            validateCreateAccount("", "luca@johnabbottcollege.net", "Luca123").name,
        )
        assertEquals(
            "Email is required.",
            validateCreateAccount("Luca", "", "Luca123").email,
        )
        assertEquals(
            "Password is required.",
            validateCreateAccount("Luca", "luca@johnabbottcollege.net", "").password,
        )
    }

    @Test
    fun nonAbbottEmailIsRejected() {
        val message = "Use a lowercase @johnabbottcollege.net email."
        val rejected =
            listOf(
                "luca@gmail.com",
                "luca@johnabbott.ca",
                "Luca@johnabbottcollege.net",
                "luca@JohnAbbottCollege.net",
                "@johnabbottcollege.net",
            )

        rejected.forEach { email ->
            val errors = validateCreateAccount("Luca", email, "Luca123")
            assertEquals(message, errors.email, email)
            assertTrue(errors.any)
        }
    }

    @Test
    fun validLoginHasNoErrors() {
        val errors = validateLogin("luca@johnabbottcollege.net", "Luca123")

        assertFalse(errors.any)
        assertNull(errors.email)
        assertNull(errors.password)
    }

    @Test
    fun loginRequiresAValidEmailAndPassword() {
        val blank = validateLogin(" ", " ")
        assertEquals("Email is required.", blank.email)
        assertEquals("Password is required.", blank.password)
        assertTrue(blank.any)

        val missingPassword = validateLogin("luca@johnabbottcollege.net", "")
        assertNull(missingPassword.email)
        assertEquals("Password is required.", missingPassword.password)

        val outsideEmail = validateLogin("luca@gmail.com", "Luca123")
        assertEquals("Use a lowercase @johnabbottcollege.net email.", outsideEmail.email)
        assertNull(outsideEmail.password)
        assertTrue(outsideEmail.any)
    }

    @Test
    fun loginAcceptsOnlyMatchingDemoCredentials() {
        assertTrue(authenticate("luca@johnabbottcollege.net", "Luca123"))
        assertFalse(authenticate("luca@johnabbottcollege.net", "wrong"))
        assertFalse(authenticate("nobody@johnabbottcollege.net", "Luca123"))
        assertFalse(authenticate("luca@gmail.com", "Luca123"))
    }
}
