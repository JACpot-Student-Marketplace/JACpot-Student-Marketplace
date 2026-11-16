package com.example.multiplatformapplication.auth

import android.content.Context

private const val PREFS = "jacpot.session"
private const val KEY = "session"

private var appContext: Context? = null

fun installSessionStore(context: Context) {
    appContext = context.applicationContext
}

private class AndroidSessionStore(
    context: Context,
) : SessionStore {
    private val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    override fun load(): String? = prefs.getString(KEY, null)

    override fun save(value: String) {
        prefs.edit().putString(KEY, value).apply()
    }

    override fun clear() {
        prefs.edit().remove(KEY).apply()
    }
}

private class MemorySessionStore : SessionStore {
    private var value: String? = null

    override fun load(): String? = value

    override fun save(value: String) {
        this.value = value
    }

    override fun clear() {
        value = null
    }
}

actual fun createSessionStore(): SessionStore {
    val context = appContext
    return if (context == null) MemorySessionStore() else AndroidSessionStore(context)
}
