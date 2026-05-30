package com.harsh.rentalconnect.domain.usecase

import com.harsh.rentalconnect.domain.model.ValidationError
import com.harsh.rentalconnect.domain.model.ValidationResult

class ValidateNameUseCase {
    operator fun invoke(name: String): ValidationResult {
        val trimmed = name.trim()
        return when {
            trimmed.isBlank() -> ValidationResult.Invalid(ValidationError.EMPTY)
            trimmed.length < 2 -> ValidationResult.Invalid(ValidationError.TOO_SHORT)
            trimmed.length > 50 -> ValidationResult.Invalid(ValidationError.TOO_LONG)
            !trimmed.matches(Regex("^[a-zA-Z\\s.'-]+$")) -> ValidationResult.Invalid(ValidationError.INVALID_FORMAT)
            else -> ValidationResult.Valid
        }
    }
}
