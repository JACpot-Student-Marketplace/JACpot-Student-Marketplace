package com.example.multiplatformapplication.ui.screens

import com.example.multiplatformapplication.model.validateEmail

internal data class CreateAccountErrors(
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val password: String? = null,
) {
    val any get() = listOfNotNull(firstName, lastName, email, password).isNotEmpty()
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
    firstName: String,
    lastName: String,
    email: String,
    password: String,
): CreateAccountErrors =
    CreateAccountErrors(
        firstName = "First name is required.".takeIf { firstName.isBlank() },
        lastName = "Last name is required.".takeIf { lastName.isBlank() },
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

internal data class ChangePasswordErrors(
    val currentPassword: String? = null,
    val newPassword: String? = null,
) {
    val any get() = listOfNotNull(currentPassword, newPassword).isNotEmpty()
}

internal fun validateChangePassword(
    currentPassword: String,
    newPassword: String,
): ChangePasswordErrors =
    ChangePasswordErrors(
        currentPassword = "Current password is required.".takeIf { currentPassword.isBlank() },
        newPassword = "Password is required.".takeIf { newPassword.isBlank() },
    )
