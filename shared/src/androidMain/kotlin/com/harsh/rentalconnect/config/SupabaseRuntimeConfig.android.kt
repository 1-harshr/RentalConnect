package com.harsh.rentalconnect.config

actual fun loadSupabaseRuntimeConfig(): SupabaseRuntimeConfig = SupabaseRuntimeConfig(
    url = SupabaseConfigHolder.url,
    anonKey = SupabaseConfigHolder.anonKey,
)
