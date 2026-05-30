package com.harsh.rentalconnect.config

data class SupabaseRuntimeConfig(
    val url: String = "",
    val anonKey: String = "",
) {
    val isConfigured: Boolean
        get() = url.isNotBlank() && anonKey.isNotBlank()
}

expect fun loadSupabaseRuntimeConfig(): SupabaseRuntimeConfig
