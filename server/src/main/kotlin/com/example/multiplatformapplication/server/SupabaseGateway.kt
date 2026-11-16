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
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.Instant
import java.util.UUID

class SupabaseGateway(
    supabaseUrl: String,
    secret: String,
) : StudentStore {
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

    override suspend fun users(): List<User> {
        val rows: List<UserRow> =
            http
                .get("$rest/users") {
                    parameter("select", PUBLIC_USER_COLUMNS)
                }.body()
        return rows.map { it.toUser() }
    }

    override suspend fun findByEmail(email: String): StoredStudent? {
        val response =
            http.get("$rest/users") {
                parameter("select", CREDENTIAL_COLUMNS)
                parameter("email", "eq.$email")
                parameter("limit", "1")
            }
        if (!response.status.isSuccess()) error("Could not look up user (${response.status.value})")
        val rows: List<CredentialRow> = response.body()
        return rows.firstOrNull()?.toStored()
    }

    override suspend fun insertStudent(
        firstName: String,
        lastName: String,
        email: String,
        passwordHash: String,
    ): User {
        val response =
            http.post("$rest/users") {
                header("Prefer", "return=representation")
                contentType(ContentType.Application.Json)
                setBody(
                    UserInsert(
                        id = UUID.randomUUID().toString(),
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        passwordHash = passwordHash,
                        createdAt = Instant.now().toString(),
                    ),
                )
            }
        if (response.status == HttpStatusCode.Conflict) {
            response.bodyAsText()
            throw DuplicateEmailException()
        }
        if (!response.status.isSuccess()) {
            val duplicate = response.bodyAsText().contains("23505")
            if (duplicate) throw DuplicateEmailException()
            error("Could not create user (${response.status.value})")
        }
        val rows: List<UserRow> = response.body()
        return rows.first().toUser()
    }

    override suspend fun saveSession(
        tokenHash: String,
        userId: String,
        expiresAt: Long,
    ) {
        val response =
            http.post("$rest/sessions") {
                contentType(ContentType.Application.Json)
                setBody(
                    SessionInsert(
                        tokenHash = tokenHash,
                        userId = userId,
                        expiresAt = Instant.ofEpochMilli(expiresAt).toString(),
                    ),
                )
            }
        response.bodyAsText()
        if (!response.status.isSuccess()) error("Could not save session (${response.status.value})")
    }

    override suspend fun findValidSession(
        tokenHash: String,
        nowMillis: Long,
    ): StudentPrincipal? {
        val response =
            http.get("$rest/sessions") {
                parameter("select", "user_id,expires_at")
                parameter("token_hash", "eq.$tokenHash")
                parameter("limit", "1")
            }
        if (!response.status.isSuccess()) error("Could not look up session (${response.status.value})")
        val row = response.body<List<SessionRow>>().firstOrNull() ?: return null
        val expiresMillis = runCatching { Instant.parse(row.expiresAt).toEpochMilli() }.getOrNull() ?: return null
        if (expiresMillis <= nowMillis) return null
        val user = findPublicUser(row.userId) ?: return null
        return StudentPrincipal(userId = user.id, user = user)
    }

    override suspend fun updatePassword(
        userId: String,
        passwordHash: String,
    ) {
        val response =
            http.patch("$rest/users") {
                parameter("id", "eq.$userId")
                contentType(ContentType.Application.Json)
                setBody(PasswordUpdate(passwordHash))
            }
        response.bodyAsText()
        if (!response.status.isSuccess()) error("Could not update password (${response.status.value})")
    }

    override suspend fun deleteSession(tokenHash: String) {
        val response =
            http.delete("$rest/sessions") {
                parameter("token_hash", "eq.$tokenHash")
            }
        response.bodyAsText()
        if (!response.status.isSuccess()) error("Could not delete session (${response.status.value})")
    }

    override suspend fun listings(): List<Listing> {
        val rows: List<ListingRow> =
            http
                .get("$rest/listings") {
                    parameter("select", LISTING_COLUMNS)
                }.body()
        return rows.map { it.toListing() }
    }

    override suspend fun createListing(listing: NewListing): Listing {
        val rows: List<ListingRow> =
            http
                .post("$rest/listings") {
                    header("Prefer", "return=representation")
                    contentType(ContentType.Application.Json)
                    setBody(listing.toInsert())
                }.body()
        return rows.first().toListing()
    }

    private suspend fun findPublicUser(id: String): User? {
        val response =
            http.get("$rest/users") {
                parameter("select", PUBLIC_USER_COLUMNS)
                parameter("id", "eq.$id")
                parameter("limit", "1")
            }
        if (!response.status.isSuccess()) error("Could not look up user (${response.status.value})")
        return response.body<List<UserRow>>().firstOrNull()?.toUser()
    }
}

private const val PUBLIC_USER_COLUMNS =
    "id,first_name,last_name,email,profile_photo_url,created_at"

private const val CREDENTIAL_COLUMNS = "$PUBLIC_USER_COLUMNS,password_hash"

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
private data class CredentialRow(
    val id: String,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    val email: String,
    @SerialName("profile_photo_url") val profilePhotoUrl: String? = null,
    @SerialName("created_at") val createdAt: String,
    @SerialName("password_hash") val passwordHash: String? = null,
) {
    fun toStored(): StoredStudent =
        StoredStudent(
            user =
                UserRow(
                    id = id,
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    profilePhotoUrl = profilePhotoUrl,
                    createdAt = createdAt,
                ).toUser(),
            passwordHash = passwordHash,
        )
}

@Serializable
private data class UserInsert(
    val id: String,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    val email: String,
    @SerialName("password_hash") val passwordHash: String,
    @SerialName("created_at") val createdAt: String,
)

@Serializable
private data class SessionInsert(
    @SerialName("token_hash") val tokenHash: String,
    @SerialName("user_id") val userId: String,
    @SerialName("expires_at") val expiresAt: String,
)

@Serializable
private data class SessionRow(
    @SerialName("user_id") val userId: String,
    @SerialName("expires_at") val expiresAt: String,
)

@Serializable
private data class PasswordUpdate(
    @SerialName("password_hash") val passwordHash: String,
)

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
