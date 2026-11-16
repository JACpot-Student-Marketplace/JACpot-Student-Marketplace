package com.example.multiplatformapplication.api

import com.example.multiplatformapplication.model.ApiError
import com.example.multiplatformapplication.model.AuthResponse
import com.example.multiplatformapplication.model.ChangePasswordRequest
import com.example.multiplatformapplication.model.Listing
import com.example.multiplatformapplication.model.LoginRequest
import com.example.multiplatformapplication.model.NewListing
import com.example.multiplatformapplication.model.RegisterRequest
import com.example.multiplatformapplication.model.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ApiException(
    message: String,
) : RuntimeException(message)

class JacpotApi(
    private val http: HttpClient,
) {
    suspend fun users(): List<User> = http.get("/users").bodyOrThrow()

    suspend fun listings(): List<Listing> = http.get("/listings").bodyOrThrow()

    suspend fun createListing(listing: NewListing): Listing =
        http
            .post("/listings") {
                contentType(ContentType.Application.Json)
                setBody(listing)
            }.bodyOrThrow()

    suspend fun register(request: RegisterRequest): User =
        http
            .post("/register") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.bodyOrThrow()

    suspend fun login(request: LoginRequest): AuthResponse =
        http
            .post("/login") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.bodyOrThrow()

    suspend fun changePassword(request: ChangePasswordRequest) {
        http
            .post("/password") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.requireSuccess()
    }

    suspend fun logout() {
        http.delete("/session").requireSuccess()
    }
}

fun createApiClient(
    baseUrl: String = apiBaseUrl(),
    token: () -> String? = { null },
    http: HttpClient = createPlatformHttpClient(),
): JacpotApi {
    val configured =
        http.config {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            defaultRequest {
                url(baseUrl)
                token()?.let { header(HttpHeaders.Authorization, "Bearer $it") }
            }
        }
    return JacpotApi(configured)
}

private suspend inline fun <reified T> HttpResponse.bodyOrThrow(): T {
    requireSuccess()
    return body()
}

private suspend fun HttpResponse.requireSuccess() {
    if (status.isSuccess()) return
    val message =
        runCatching { Json.decodeFromString<ApiError>(bodyAsText()).error }.getOrNull()
            ?: "Could not reach JACpot. Try again."
    throw ApiException(message)
}
