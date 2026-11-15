package com.example.multiplatformapplication.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

actual fun createPlatformHttpClient(): HttpClient = HttpClient(CIO)

actual fun apiBaseUrl(): String = "http://127.0.0.1:8080"
