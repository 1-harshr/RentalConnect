package com.harsh.rentalconnect.config

import platform.Foundation.NSProcessInfo

actual fun loadSupabaseRuntimeConfig(): SupabaseRuntimeConfig {
    val environment = NSProcessInfo.processInfo.environment
    return SupabaseRuntimeConfig(
        url = (environment["SUPABASE_URL"] as? String).orEmpty(),
        anonKey = (environment["SUPABASE_ANON_KEY"] as? String).orEmpty(),
    )
}
