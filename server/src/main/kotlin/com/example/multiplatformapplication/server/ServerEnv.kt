package com.example.multiplatformapplication.server

import java.io.File

data class ServerEnv(
    val supabaseUrl: String,
    val supabaseSecret: String,
)

fun loadServerEnv(): ServerEnv {
    val values = mutableMapOf<String, String>()
    listOf(File("server/.env"), File(".env")).forEach { file ->
        if (!file.exists()) return@forEach
        file.readLines().forEach { line ->
            val trimmed = line.trim()
            if (trimmed.isEmpty() || trimmed.startsWith("#")) return@forEach
            val parts = trimmed.split("=", limit = 2)
            if (parts.size == 2) values[parts[0].trim()] = parts[1].trim()
        }
    }

    fun value(name: String): String =
        System.getenv(name)?.takeIf { it.isNotBlank() }
            ?: values[name]?.takeIf { it.isNotBlank() }
            ?: error("Missing $name. Set it in server/.env")

    return ServerEnv(
        supabaseUrl = value("SUPABASE_URL"),
        supabaseSecret = value("SUPABASE_SECRET_KEY"),
    )
}
