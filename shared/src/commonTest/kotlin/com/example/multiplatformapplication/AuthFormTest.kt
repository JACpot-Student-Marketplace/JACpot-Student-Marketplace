package com.example.multiplatformapplication

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
                firstName = "Luca",
                lastName = "Maiolo",
                email = "luca@johnabbottcollege.net",
                password = "Luca123",
            )

        assertFalse(errors.any)
        assertNull(errors.firstName)
        assertNull(errors.lastName)
        assertNull(errors.email)
        assertNull(errors.password)
    }

    @Test
    fun trimmedAbbottEmailIsAccepted() {
        val errors =
            validateCreateAccount(
                firstName = "Luca",
                lastName = "Maiolo",
                email = "  luca@johnabbottcollege.net  ",
                password = "Luca123",
            )

        assertFalse(errors.any)
    }

    @Test
    fun createAccountRequiresNameEmailAndPassword() {
        val errors = validateCreateAccount(firstName = " ", lastName = " ", email = " ", password = " ")

        assertTrue(errors.any)
        assertEquals("First name is required.", errors.firstName)
        assertEquals("Last name is required.", errors.lastName)
        assertEquals("Email is required.", errors.email)
        assertEquals("Password is required.", errors.password)
    }

    @Test
    fun eachCreateAccountFieldIsRequiredOnItsOwn() {
        assertEquals(
            "First name is required.",
            validateCreateAccount("", "Maiolo", "luca@johnabbottcollege.net", "Luca123").firstName,
        )
        assertEquals(
            "Last name is required.",
            validateCreateAccount("Luca", "", "luca@johnabbottcollege.net", "Luca123").lastName,
        )
        assertEquals(
            "Email is required.",
            validateCreateAccount("Luca", "Maiolo", "", "Luca123").email,
        )
        assertEquals(
            "Password is required.",
            validateCreateAccount("Luca", "Maiolo", "luca@johnabbottcollege.net", "").password,
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
            val errors = validateCreateAccount("Luca", "Maiolo", email, "Luca123")
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
}
