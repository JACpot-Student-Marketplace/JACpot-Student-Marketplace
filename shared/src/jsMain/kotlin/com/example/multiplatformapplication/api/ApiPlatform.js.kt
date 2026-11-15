package com.example.multiplatformapplication.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js

actual fun createPlatformHttpClient(): HttpClient = HttpClient(Js)

actual fun apiBaseUrl(): String = "http://127.0.0.1:8080"
