package com.harsh.rentalconnect.domain.usecase

import com.harsh.rentalconnect.domain.model.ValidationError
import com.harsh.rentalconnect.domain.model.ValidationResult

class ValidatePhoneUseCase {
    operator fun invoke(phone: String): ValidationResult {
        val stripped = phone.trim()
            .replace(Regex("[\\s\\-()]"), "")
            .removePrefix("+91")
        return when {
            stripped.isBlank() -> ValidationResult.Invalid(ValidationError.EMPTY)
            !stripped.all { it.isDigit() } -> ValidationResult.Invalid(ValidationError.INVALID_FORMAT)
            stripped.length != 10 -> ValidationResult.Invalid(ValidationError.INVALID_FORMAT)
            else -> ValidationResult.Valid
        }
    }
}
