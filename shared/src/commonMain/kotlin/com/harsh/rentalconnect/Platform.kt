package com.harsh.rentalconnect

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform