package com.harsh.rentalconnect.config

actual fun loadSupabaseRuntimeConfig(): SupabaseRuntimeConfig = SupabaseRuntimeConfig(
    url = System.getenv("SUPABASE_URL").orEmpty(),
    anonKey = System.getenv("SUPABASE_ANON_KEY").orEmpty(),
)
