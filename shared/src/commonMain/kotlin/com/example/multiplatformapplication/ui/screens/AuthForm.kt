package com.example.multiplatformapplication.ui.screens

import com.example.multiplatformapplication.model.validateEmail

internal data class CreateAccountErrors(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
) {
    val any get() = listOfNotNull(name, email, password).isNotEmpty()
}

internal data class LoginErrors(
    val email: String? = null,
    val password: String? = null,
) {
    val any get() = listOfNotNull(email, password).isNotEmpty()
}

private fun emailFieldError(email: String): String? {
    val clean = email.trim()
    if (clean.isEmpty()) return "Email is required."
    return validateEmail(clean)
}

internal fun validateCreateAccount(
    name: String,
    email: String,
    password: String,
): CreateAccountErrors =
    CreateAccountErrors(
        name = "Name is required.".takeIf { name.isBlank() },
        email = emailFieldError(email),
        password = "Password is required.".takeIf { password.isBlank() },
    )

internal fun validateLogin(
    email: String,
    password: String,
): LoginErrors =
    LoginErrors(
        email = emailFieldError(email),
        password = "Password is required.".takeIf { password.isBlank() },
    )
