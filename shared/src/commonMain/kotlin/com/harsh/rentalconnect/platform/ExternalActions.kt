package com.harsh.rentalconnect.platform

expect fun openExternalUrl(url: String)

fun openDialer(phone: String) {
    val digits = phone.filter { it.isDigit() || it == '+' }
    if (digits.isNotBlank()) {
        openExternalUrl("tel:$digits")
    }
}

fun openSms(phone: String, message: String? = null) {
    val digits = phone.filter { it.isDigit() || it == '+' }
    if (digits.isBlank()) return
    val body = message?.takeIf { it.isNotBlank() }?.let {
        "?body=${encodeUrlComponent(it)}"
    }.orEmpty()
    openExternalUrl("sms:$digits$body")
}

fun openMapQuery(query: String) {
    if (query.isNotBlank()) {
        openExternalUrl("https://www.google.com/maps/search/?api=1&query=${encodeUrlComponent(query)}")
    }
}

private fun encodeUrlComponent(value: String): String =
    value
        .replace("%", "%25")
        .replace(" ", "%20")
        .replace("\n", "%0A")
        .replace(",", "%2C")
        .replace("+", "%2B")
