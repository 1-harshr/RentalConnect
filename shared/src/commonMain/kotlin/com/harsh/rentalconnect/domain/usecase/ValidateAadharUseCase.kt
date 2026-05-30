package com.harsh.rentalconnect.domain.usecase

import com.harsh.rentalconnect.domain.model.AuthValidationError

class ValidateAadharUseCase {
    operator fun invoke(aadharId: String): AuthValidationError? {
        val digits = aadharId.filter { it.isDigit() }
        return when {
            digits.isEmpty() -> AuthValidationError.EMPTY
            digits.length != 12 -> AuthValidationError.INVALID_FORMAT
            else -> null
        }
    }
}
