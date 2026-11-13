package com.example.multiplatformapplication.api

import com.example.multiplatformapplication.model.Listing
import com.example.multiplatformapplication.model.NewListing
import com.example.multiplatformapplication.model.User
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.url

class JacpotApi(
    private val http: io.ktor.client.HttpClient,
) {
    suspend fun users(): List<User> = http.get("/users").body()

    suspend fun listings(): List<Listing> = http.get("/listings").body()

    suspend fun createListing(listing: NewListing): Listing =
        http.post("/listings") {
            contentType(ContentType.Application.Json)
            setBody(listing)
        }.body()
}

fun createApiClient(baseUrl: String = apiBaseUrl()): JacpotApi {
    val http =
        createPlatformHttpClient().config {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            defaultRequest {
                url(baseUrl)
            }
        }
    return JacpotApi(http)
}