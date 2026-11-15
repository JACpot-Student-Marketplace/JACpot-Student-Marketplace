package com.example.multiplatformapplication.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

actual fun createPlatformHttpClient(): HttpClient = HttpClient(OkHttp)

actual fun apiBaseUrl(): String = "http://10.0.2.2:8080"
