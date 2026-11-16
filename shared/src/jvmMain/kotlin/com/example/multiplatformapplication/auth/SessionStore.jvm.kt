package com.example.multiplatformapplication.auth

import java.util.prefs.Preferences

private class DesktopSessionStore : SessionStore {
    private val prefs = Preferences.userRoot().node("jacpot.session")

    override fun load(): String? = prefs.get("session", "").ifBlank { null }

    override fun save(value: String) {
        prefs.put("session", value)
        prefs.flush()
    }

    override fun clear() {
        prefs.remove("session")
        prefs.flush()
    }
}

actual fun createSessionStore(): SessionStore = DesktopSessionStore()
