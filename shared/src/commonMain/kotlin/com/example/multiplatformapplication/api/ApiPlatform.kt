package com.example.multiplatformapplication.api

import io.ktor.client.HttpClient

expect fun createPlatformHttpClient(): HttpClient

expect fun apiBaseUrl(): String