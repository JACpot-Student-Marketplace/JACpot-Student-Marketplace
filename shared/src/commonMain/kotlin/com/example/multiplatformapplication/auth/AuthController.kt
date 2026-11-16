package com.example.multiplatformapplication.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.multiplatformapplication.api.JacpotApi
import com.example.multiplatformapplication.api.createApiClient
import com.example.multiplatformapplication.model.AuthResponse
import kotlinx.serialization.json.Json
import kotlin.time.Clock

class AuthController(
    private val store: SessionStore,
    private val json: Json = Json { ignoreUnknownKeys = true },
) {
    var session by mutableStateOf(load())
        private set

    val api: JacpotApi = createApiClient(token = { session?.token })

    fun save(response: AuthResponse) {
        store.save(json.encodeToString(response))
        session = response
    }

    fun clear() {
        store.clear()
        session = null
    }

    private fun load(): AuthResponse? {
        val raw = store.load()
        val restored = restoreSession(raw, Clock.System.now().toEpochMilliseconds(), json)
        if (raw != null && restored == null) store.clear()
        return restored
    }
}

internal fun restoreSession(
    raw: String?,
    now: Long,
    json: Json = Json { ignoreUnknownKeys = true },
): AuthResponse? {
    if (raw.isNullOrBlank()) return null
    val parsed = runCatching { json.decodeFromString<AuthResponse>(raw) }.getOrNull() ?: return null
    return parsed.takeIf { it.expiresAt > now }
}
