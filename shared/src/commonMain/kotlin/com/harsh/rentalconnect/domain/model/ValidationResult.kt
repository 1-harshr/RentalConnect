package com.harsh.rentalconnect.domain.model

sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Invalid(val error: ValidationError) : ValidationResult()
}

enum class ValidationError {
    EMPTY,
    TOO_SHORT,
    TOO_LONG,
    INVALID_FORMAT,
}
