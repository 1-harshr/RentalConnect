package com.harsh.rentalconnect.domain.repository

import com.harsh.rentalconnect.domain.model.AuthUser
import com.harsh.rentalconnect.domain.model.AuthResult
import com.harsh.rentalconnect.domain.model.AuthSession
import com.harsh.rentalconnect.domain.model.ProfileUpdateRequest
import com.harsh.rentalconnect.domain.model.SignInRequest
import com.harsh.rentalconnect.domain.model.SignUpRequest
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val session: StateFlow<AuthSession?>

    suspend fun signIn(request: SignInRequest): AuthResult

    suspend fun signUp(request: SignUpRequest): AuthResult

    suspend fun signOut()

    suspend fun updateProfile(request: ProfileUpdateRequest): AuthResult

    suspend fun findTenantByPhone(phone: String): AuthUser?

    suspend fun linkTenantRecord(userId: String, tenantId: String)
}
