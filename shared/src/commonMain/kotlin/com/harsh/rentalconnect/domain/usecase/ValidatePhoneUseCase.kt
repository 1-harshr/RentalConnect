package com.harsh.rentalconnect.domain.usecase

import com.harsh.rentalconnect.domain.model.ValidationError
import com.harsh.rentalconnect.domain.model.ValidationResult

class ValidatePhoneUseCase {

    // Local number lengths per dial code (excluding the country prefix itself)
    private val localLengths: Map<String, Int> = mapOf(
        "+91" to 10,  // India
        "+1" to 10,   // US / Canada
        "+44" to 10,  // UK
        "+61" to 9,   // Australia
        "+65" to 8,   // Singapore
        "+971" to 9,  // UAE
        "+49" to 11,  // Germany
        "+33" to 9,   // France
        "+81" to 11,  // Japan
    )

    operator fun invoke(phone: String, dialCode: String = "+91"): ValidationResult {
        val stripped = phone.trim().replace(Regex("[\\s\\-()]"), "")
        val expected = localLengths[dialCode] ?: 10
        return when {
            stripped.isBlank() -> ValidationResult.Invalid(ValidationError.EMPTY)
            !stripped.all { it.isDigit() } -> ValidationResult.Invalid(ValidationError.INVALID_FORMAT)
            stripped.length != expected -> ValidationResult.Invalid(ValidationError.INVALID_FORMAT)
            else -> ValidationResult.Valid
        }
    }
}
