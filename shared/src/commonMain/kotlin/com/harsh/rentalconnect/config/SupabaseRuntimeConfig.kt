package com.harsh.rentalconnect.config

data class SupabaseRuntimeConfig(
    val url: String = "",
    val anonKey: String = "",
) {
    val isConfigured: Boolean
        get() = url.isNotBlank() && anonKey.isNotBlank()
}

expect fun loadSupabaseRuntimeConfig(): SupabaseRuntimeConfig

// Populated by the host app before the first loadSupabaseRuntimeConfig() call.
// Android: MainActivity writes BuildConfig values here at startup.
object SupabaseConfigHolder {
    var url: String = ""
    var anonKey: String = ""
}
