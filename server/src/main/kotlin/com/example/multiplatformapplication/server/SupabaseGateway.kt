package com.example.multiplatformapplication.server

import com.example.multiplatformapplication.model.ItemCondition
import com.example.multiplatformapplication.model.Listing
import com.example.multiplatformapplication.model.ListingCategory
import com.example.multiplatformapplication.model.ListingStatus
import com.example.multiplatformapplication.model.NewListing
import com.example.multiplatformapplication.model.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import java.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class SupabaseGateway(
    supabaseUrl: String,
    secret: String,
) {
    private val rest = supabaseUrl.trimEnd('/') + "/rest/v1"
    private val http =
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            defaultRequest {
                header(HttpHeaders.Authorization, "Bearer $secret")
                header("apikey", secret)
            }
        }

    suspend fun users(): List<User> {
        val rows: List<UserRow> =
            http.get("$rest/users") {
                parameter(
                    "select",
                    "id,first_name,last_name,email,profile_photo_url,created_at",
                )
            }.body()
        return rows.map { it.toUser() }
    }

    suspend fun listings(): List<Listing> {
        val rows: List<ListingRow> =
            http.get("$rest/listings") {
                parameter("select", LISTING_COLUMNS)
            }.body()
        return rows.map { it.toListing() }
    }

    suspend fun createListing(listing: NewListing): Listing {
        val rows: List<ListingRow> =
            http.post("$rest/listings") {
                header("Prefer", "return=representation")
                contentType(ContentType.Application.Json)
                setBody(listing.toInsert())
            }.body()
        return rows.first().toListing()
    }
}

private const val LISTING_COLUMNS =
    "id,owner_id,title,description,category,condition,price_cents,course_code,author,release_date,access_code_used,status,created_at,updated_at"

@Serializable
private data class UserRow(
    val id: String,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    val email: String,
    @SerialName("profile_photo_url") val profilePhotoUrl: String? = null,
    @SerialName("created_at") val createdAt: String,
) {
    fun toUser(): User =
        User(
            id = id,
            firstName = firstName,
            lastName = lastName,
            email = email,
            profilePhotoUrl = profilePhotoUrl,
            createdAt = Instant.parse(createdAt).toEpochMilli(),
        )
}

@Serializable
private data class ListingInsert(
    @SerialName("owner_id") val ownerId: String,
    val title: String,
    val description: String,
    val category: ListingCategory,
    val condition: ItemCondition,
    @SerialName("price_cents") val priceCents: Long,
    @SerialName("course_code") val courseCode: String? = null,
    val author: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("access_code_used") val accessCodeUsed: Boolean? = null,
)

@Serializable
private data class ListingRow(
    val id: String,
    @SerialName("owner_id") val ownerId: String,
    val title: String,
    val description: String,
    val category: ListingCategory,
    val condition: ItemCondition,
    @SerialName("price_cents") val priceCents: Long,
    @SerialName("course_code") val courseCode: String? = null,
    val author: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("access_code_used") val accessCodeUsed: Boolean? = null,
    val status: ListingStatus,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
) {
    fun toListing(): Listing =
        Listing(
            id = id,
            ownerId = ownerId,
            title = title,
            description = description,
            category = category,
            condition = condition,
            priceCents = priceCents,
            courseCode = courseCode,
            author = author,
            releaseDate = releaseDate,
            accessCodeUsed = accessCodeUsed,
            status = status,
            createdAt = Instant.parse(createdAt).toEpochMilli(),
            updatedAt = Instant.parse(updatedAt).toEpochMilli(),
        )
}

private fun NewListing.toInsert(): ListingInsert =
    ListingInsert(
        ownerId = ownerId,
        title = title,
        description = description,
        category = category,
        condition = condition,
        priceCents = priceCents,
        courseCode = courseCode,
        author = author,
        releaseDate = releaseDate,
        accessCodeUsed = accessCodeUsed,
    )
