package com.example.multiplatformapplication.server

import com.example.multiplatformapplication.model.ApiError
import com.example.multiplatformapplication.model.AuthResponse
import com.example.multiplatformapplication.model.ChangePasswordRequest
import com.example.multiplatformapplication.model.LoginRequest
import com.example.multiplatformapplication.model.RegisterRequest
import com.example.multiplatformapplication.model.validateEmail
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.post

fun Route.registerRoute(
    store: StudentStore,
    hasher: PasswordHasher,
) {
    post("/register") {
        val body = call.receive<RegisterRequest>()
        val email = body.email.trim()
        val firstName = body.firstName.trim()
        val lastName = body.lastName.trim()
        val errorMessage = registrationError(email, firstName, lastName, body.password)
        if (errorMessage != null) {
            call.respond(HttpStatusCode.BadRequest, ApiError(errorMessage))
            return@post
        }
        if (store.findByEmail(email) != null) {
            call.respond(HttpStatusCode.Conflict, ApiError(DUPLICATE_EMAIL))
            return@post
        }
        val user =
            try {
                store.insertStudent(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    passwordHash = hasher.hash(body.password),
                )
            } catch (_: DuplicateEmailException) {
                call.respond(HttpStatusCode.Conflict, ApiError(DUPLICATE_EMAIL))
                return@post
            }
        call.respond(HttpStatusCode.Created, user)
    }
}

fun Route.loginRoute(
    store: StudentStore,
    hasher: PasswordHasher,
    clock: () -> Long,
) {
    post("/login") {
        val body = call.receive<LoginRequest>()
        val stored = store.findByEmail(body.email.trim())
        val passwordOk = hasher.matches(body.password, stored?.passwordHash)
        if (stored == null || !passwordOk) {
            call.respond(HttpStatusCode.Unauthorized, ApiError(LOGIN_FAILED))
            return@post
        }
        val token = newSessionToken()
        val expiresAt = clock() + SESSION_TTL_MILLIS
        store.saveSession(
            tokenHash = hashSessionToken(token),
            userId = stored.user.id,
            expiresAt = expiresAt,
        )
        call.respond(AuthResponse(token = token, expiresAt = expiresAt, user = stored.user))
    }
}

fun Route.passwordRoute(
    store: StudentStore,
    hasher: PasswordHasher,
) {
    post("/password") {
        val student = call.principal<StudentPrincipal>()
        if (student == null) {
            call.respond(HttpStatusCode.Unauthorized)
            return@post
        }
        val body = call.receive<ChangePasswordRequest>()
        if (body.newPassword.isBlank()) {
            call.respond(HttpStatusCode.BadRequest, ApiError("Password is required."))
            return@post
        }
        val stored = store.findByEmail(student.user.email)
        val currentOk =
            stored != null &&
                stored.user.id == student.userId &&
                hasher.matches(body.currentPassword, stored.passwordHash)
        if (!currentOk) {
            call.respond(HttpStatusCode.Unauthorized, ApiError("Current password is incorrect."))
            return@post
        }
        store.updatePassword(student.userId, hasher.hash(body.newPassword))
        call.respond(HttpStatusCode.OK)
    }
}

fun Route.logoutRoute(store: StudentStore) {
    delete("/session") {
        val header = call.request.headers[HttpHeaders.Authorization]
        val token = header?.removePrefix("Bearer ")?.trim().orEmpty()
        if (token.isNotEmpty()) store.deleteSession(hashSessionToken(token))
        call.respond(HttpStatusCode.NoContent)
    }
}

private fun registrationError(
    email: String,
    firstName: String,
    lastName: String,
    password: String,
): String? {
    validateEmail(email)?.let { return it }
    if (firstName.isBlank()) return "First name is required."
    if (lastName.isBlank()) return "Last name is required."
    if (password.isBlank()) return "Password is required."
    return null
}

private const val DUPLICATE_EMAIL = "An account with this email already exists."

private const val LOGIN_FAILED = "Wrong email or password."
