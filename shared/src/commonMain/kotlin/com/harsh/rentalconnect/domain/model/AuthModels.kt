package com.harsh.rentalconnect.domain.model

import com.harsh.rentalconnect.ui.models.Role

data class AuthUser(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: Role,
    val hometown: String,
    val aadharId: String,
    val linkedTenantIds: List<String> = emptyList(),
)

data class AuthSession(
    val accessToken: String,
    val user: AuthUser,
)

data class SignUpRequest(
    val name: String,
    val email: String,
    val phone: String,
    val hometown: String,
    val aadharId: String,
    val password: String,
    val role: Role,
)

data class ProfileUpdateRequest(
    val name: String,
    val phone: String,
    val hometown: String,
    val aadharId: String,
)

data class SignInRequest(
    val email: String,
    val password: String,
)

sealed class AuthResult {
    data class Success(val session: AuthSession) : AuthResult()
    data class Failure(val error: AuthError) : AuthResult()
}

enum class AuthError {
    INVALID_CREDENTIALS,
    EMAIL_ALREADY_IN_USE,
    PHONE_ALREADY_IN_USE,
    NETWORK,
    BACKEND_NOT_CONFIGURED,
    UNKNOWN,
}

enum class AuthValidationError {
    EMPTY,
    INVALID_FORMAT,
    TOO_SHORT,
    MISMATCH,
}
