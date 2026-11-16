package com.example.multiplatformapplication.auth

import kotlinx.browser.localStorage

private const val KEY = "jacpot.session"

private class BrowserSessionStore : SessionStore {
    override fun load(): String? = localStorage.getItem(KEY)

    override fun save(value: String) {
        localStorage.setItem(KEY, value)
    }

    override fun clear() {
        localStorage.removeItem(KEY)
    }
}

actual fun createSessionStore(): SessionStore = BrowserSessionStore()
