package com.example.multiplatformapplication.auth

interface SessionStore {
    fun load(): String?

    fun save(value: String)

    fun clear()
}

expect fun createSessionStore(): SessionStore
