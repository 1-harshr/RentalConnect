package com.harsh.rentalconnect.domain.usecase

import com.harsh.rentalconnect.domain.model.AuthValidationError

class ValidateEmailUseCase {
    operator fun invoke(email: String): AuthValidationError? {
        val value = email.trim()
        return when {
            value.isEmpty() -> AuthValidationError.EMPTY
            !EMAIL_REGEX.matches(value) -> AuthValidationError.INVALID_FORMAT
            else -> null
        }
    }

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
