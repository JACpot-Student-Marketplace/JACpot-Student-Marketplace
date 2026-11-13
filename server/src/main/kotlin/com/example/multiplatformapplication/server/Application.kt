package com.example.multiplatformapplication.server

import com.example.multiplatformapplication.model.NewListing
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
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
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module(env)
    }.start(wait = true)
}

fun Application.module(env: ServerEnv) {
    val gateway = SupabaseGateway(env.supabaseUrl, env.supabaseSecret)
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
        anyHost()
    }
    routing {
        get("/") {
            call.respondText("JACpot API")
        }
        get("/users") {
            call.respond(gateway.users())
        }
        get("/listings") {
            call.respond(gateway.listings())
        }
        post("/listings") {
            val body = call.receive<NewListing>()
            call.respond(gateway.createListing(body))
        }
    }
}
