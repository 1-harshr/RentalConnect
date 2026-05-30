package com.harsh.rentalconnect.domain.usecase

import com.harsh.rentalconnect.domain.model.AuthValidationError

class ValidatePasswordUseCase {
    operator fun invoke(password: String): AuthValidationError? = when {
        password.isBlank() -> AuthValidationError.EMPTY
        password.length < MIN_PASSWORD_LENGTH -> AuthValidationError.TOO_SHORT
        else -> null
    }

    private companion object {
        const val MIN_PASSWORD_LENGTH = 8
    }
}
