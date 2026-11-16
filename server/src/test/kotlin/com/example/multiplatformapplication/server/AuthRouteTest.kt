package com.example.multiplatformapplication.server

import com.example.multiplatformapplication.model.AuthResponse
import com.example.multiplatformapplication.model.User
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class AuthRouteTest {
    @Test
    fun registerStoresHashAndOmitsPassword() =
        authTest { store, _ ->
            val response = register()
            assertEquals(HttpStatusCode.Created, response.status)
            val text = response.bodyAsText()
            val hash = store.students.single().passwordHash!!
            assertFalse(text.contains(PASSWORD))
            assertFalse(text.contains(hash))
            assertNotEquals(PASSWORD, hash)
            assertTrue(hash.startsWith("\$2"))
            assertTrue(PasswordHasher(cost = 4).verify(PASSWORD, hash))
            val user = Json.decodeFromString<User>(text)
            assertEquals(EMAIL, user.email)
        }

    @Test
    fun registerRejectsNonCollegeEmail() =
        authTest { store, _ ->
            val response = register(email = "student@gmail.com")
            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertTrue(response.bodyAsText().contains("@johnabbottcollege.net"))
            assertEquals(0, store.students.size)
        }

    @Test
    fun registerRejectsMixedCaseEmail() =
        authTest { store, _ ->
            val response = register(email = "Ada@johnabbottcollege.net")
            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals(0, store.students.size)
        }

    @Test
    fun registerRejectsBlankPassword() =
        authTest { store, _ ->
            val response = register(password = "   ")
            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals(0, store.students.size)
        }

    @Test
    fun registerRejectsDuplicateEmail() =
        authTest { store, _ ->
            assertEquals(HttpStatusCode.Created, register().status)
            val again = register()
            assertEquals(HttpStatusCode.Conflict, again.status)
            assertFalse(again.bodyAsText().contains(PASSWORD))
            assertEquals(1, store.students.size)
        }

    @Test
    fun loginReturnsTokenForRegisteredStudent() =
        authTest { store, clock ->
            register()
            val response = login()
            assertEquals(HttpStatusCode.OK, response.status)
            val auth = Json.decodeFromString<AuthResponse>(response.bodyAsText())
            assertTrue(auth.token.isNotBlank())
            assertEquals(clock.now + SESSION_TTL_MILLIS, auth.expiresAt)
            assertEquals(EMAIL, auth.user.email)
            val session = store.sessions.single()
            assertEquals(hashSessionToken(auth.token), session.tokenHash)
            assertNotEquals(auth.token, session.tokenHash)
            assertTrue(session.tokenHash.matches(Regex("[0-9a-f]{64}")))
            val me = getAuth("/me", auth.token)
            assertEquals(HttpStatusCode.OK, me.status)
            val user = Json.decodeFromString<User>(me.bodyAsText())
            val storedStudent = store.students.single()
            assertEquals(storedStudent.user.id, user.id)
            assertEquals(EMAIL, user.email)
            assertFalse(me.bodyAsText().contains(PASSWORD))
            assertEquals(HttpStatusCode.OK, getAuth("/listings", auth.token).status)
            val listed = getAuth("/users", auth.token)
            assertEquals(HttpStatusCode.OK, listed.status)
            assertFalse(listed.bodyAsText().contains(PASSWORD))
            assertFalse(listed.bodyAsText().contains(session.tokenHash))
        }

    @Test
    fun loginRejectsWrongPassword() =
        authTest { store, _ ->
            register()
            val response = login(password = "wrong-password")
            assertEquals(HttpStatusCode.Unauthorized, response.status)
            assertFalse(response.bodyAsText().contains("\"token\""))
            assertEquals(0, store.sessions.size)
        }

    @Test
    fun loginRejectsUnknownEmail() =
        authTest { store, _ ->
            val response = login(email = "missing@johnabbottcollege.net")
            assertEquals(HttpStatusCode.Unauthorized, response.status)
            assertFalse(response.bodyAsText().contains("\"token\""))
            assertEquals(0, store.sessions.size)
        }

    @Test
    fun missingTokenIsRejected() =
        authTest { _, _ ->
            for (path in listOf("/", "/users", "/listings", "/me")) {
                assertEquals(HttpStatusCode.Unauthorized, client.get(path).status, path)
            }
            assertEquals(HttpStatusCode.Unauthorized, client.post("/listings").status)
        }

    @Test
    fun unknownTokenIsRejected() =
        authTest { _, _ ->
            assertEquals(HttpStatusCode.Unauthorized, getAuth("/listings", "not-a-session").status)
            assertEquals(HttpStatusCode.Unauthorized, getAuth("/me", "not-a-session").status)
        }

    @Test
    fun expiredTokenIsRejected() =
        authTest { _, clock ->
            register()
            val auth = Json.decodeFromString<AuthResponse>(login().bodyAsText())
            clock.now += SESSION_TTL_MILLIS + 1
            assertEquals(HttpStatusCode.Unauthorized, getAuth("/listings", auth.token).status)
            assertEquals(HttpStatusCode.Unauthorized, getAuth("/me", auth.token).status)
        }
}

private class Clock {
    var now: Long = 1_700_000_000_000L
}

private fun authTest(block: suspend ApplicationTestBuilder.(InMemoryStudentStore, Clock) -> Unit) {
    val store = InMemoryStudentStore()
    val clock = Clock()
    testApplication {
        application {
            module(
                store,
                PasswordHasher(cost = 4),
                clock = { clock.now },
            )
        }
        block(store, clock)
    }
}

private suspend fun ApplicationTestBuilder.register(
    email: String = EMAIL,
    password: String = PASSWORD,
    firstName: String = "Ada",
    lastName: String = "Lovelace",
): HttpResponse = postJson("/register", accountJson(firstName, lastName, email, password))

private suspend fun ApplicationTestBuilder.login(
    email: String = EMAIL,
    password: String = PASSWORD,
): HttpResponse = postJson("/login", """{"email":"$email","password":"$password"}""")

private suspend fun ApplicationTestBuilder.postJson(
    path: String,
    json: String,
): HttpResponse =
    client.post(path) {
        contentType(ContentType.Application.Json)
        setBody(json)
    }

private suspend fun ApplicationTestBuilder.getAuth(
    path: String,
    token: String,
): HttpResponse =
    client.get(path) {
        header(HttpHeaders.Authorization, "Bearer $token")
    }

private fun accountJson(
    firstName: String,
    lastName: String,
    email: String,
    password: String,
) = """{"firstName":"$firstName","lastName":"$lastName","email":"$email","password":"$password"}"""

private const val EMAIL = "ada@johnabbottcollege.net"

private const val PASSWORD = "s3cret-pass"
