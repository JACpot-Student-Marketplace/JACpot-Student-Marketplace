package com.example.multiplatformapplication.server

import com.example.multiplatformapplication.model.NewListing
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.bearer
import io.ktor.server.auth.principal
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json

fun main() {
    val env = loadServerEnv()
    val store = SupabaseGateway(env.supabaseUrl, env.supabaseSecret)
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module(store)
    }.start(wait = true)
}

fun Application.module(
    store: StudentStore,
    hasher: PasswordHasher = PasswordHasher(),
    clock: () -> Long = { System.currentTimeMillis() },
) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(CORS) {
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Options)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        anyHost()
    }
    install(Authentication) {
        bearer("student") {
            authenticate { credential ->
                store.findValidSession(hashSessionToken(credential.token), clock())
            }
        }
    }
    routing {
        registerRoute(store, hasher)
        loginRoute(store, hasher, clock)
        authenticate("student") {
            get("/me") {
                val student = call.principal<StudentPrincipal>()
                if (student == null) {
                    call.respond(HttpStatusCode.Unauthorized)
                } else {
                    call.respond(student.user)
                }
            }
            get("/") {
                call.respondText("JACpot API")
            }
            get("/users") {
                call.respond(store.users())
            }
            get("/listings") {
                call.respond(store.listings())
            }
            post("/listings") {
                val body = call.receive<NewListing>()
                call.respond(store.createListing(body))
            }
        }
    }
}
