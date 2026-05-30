package com.harsh.rentalconnect.data

private val PHONE_STRIP_REGEX = Regex("[\\s\\-()]")

/**
 * Normalises a phone string for comparison by stripping whitespace, dashes, and parentheses
 * while preserving the full E.164 value (e.g. `+919876543210`).
 */
internal fun normalizePhone(phone: String): String =
    phone.trim().replace(PHONE_STRIP_REGEX, "")
