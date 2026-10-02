package com.example.multiplatformapplication

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform