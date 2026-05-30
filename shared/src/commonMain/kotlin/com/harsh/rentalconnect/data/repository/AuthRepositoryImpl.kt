package com.harsh.rentalconnect.data.repository

import com.harsh.rentalconnect.data.AccountRecord
import com.harsh.rentalconnect.data.AppStore
import com.harsh.rentalconnect.data.normalizePhone
import com.harsh.rentalconnect.data.remote.SupabaseAuthGateway
import com.harsh.rentalconnect.data.remote.SupabaseDataGateway
import com.harsh.rentalconnect.domain.model.AuthError
import com.harsh.rentalconnect.domain.model.AuthResult
import com.harsh.rentalconnect.domain.model.AuthSession
import com.harsh.rentalconnect.domain.model.AuthUser
import com.harsh.rentalconnect.domain.model.ProfileUpdateRequest
import com.harsh.rentalconnect.domain.model.SignInRequest
import com.harsh.rentalconnect.domain.model.SignUpRequest
import com.harsh.rentalconnect.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthRepositoryImpl(
    private val supabaseAuthGateway: SupabaseAuthGateway,
    private val supabaseDataGateway: SupabaseDataGateway,
    private val useSupabase: Boolean,
) : AuthRepository {
    private val _session = MutableStateFlow<AuthSession?>(null)
    override val session: StateFlow<AuthSession?> = _session.asStateFlow()

    override suspend fun signIn(request: SignInRequest): AuthResult {
        val remoteResult = attemptRemote { supabaseAuthGateway.signIn(request) }
        if (remoteResult != null) {
            if (remoteResult is AuthResult.Success) {
                val syncedSession = syncRemoteSession(remoteResult.session)
                _session.value = syncedSession
                return AuthResult.Success(syncedSession)
            }
            return remoteResult
        }

        val account = AppStore.accounts.value.firstOrNull {
            it.user.email.equals(request.email.trim(), ignoreCase = true) && it.password == request.password
        } ?: return AuthResult.Failure(AuthError.INVALID_CREDENTIALS)

        val session = AuthSession(
            accessToken = "local-${account.user.id}",
            user = account.user,
        )
        _session.value = session
        return AuthResult.Success(session)
    }

    override suspend fun signUp(request: SignUpRequest): AuthResult {
        val email = request.email.trim()
        val phone = request.phone.trim()

        if (AppStore.accounts.value.any { it.user.email.equals(email, ignoreCase = true) }) {
            return AuthResult.Failure(AuthError.EMAIL_ALREADY_IN_USE)
        }
        if (AppStore.accounts.value.any { it.user.phone == phone }) {
            return AuthResult.Failure(AuthError.PHONE_ALREADY_IN_USE)
        }

        val remoteResult = attemptRemote { supabaseAuthGateway.signUp(request) }
        if (remoteResult != null) {
            if (remoteResult is AuthResult.Success) {
                val syncedUser = runCatching {
                    supabaseDataGateway.upsertUserProfile(
                        sessionToken = remoteResult.session.accessToken,
                        user = remoteResult.session.user.copy(
                            name = request.name.trim(),
                            phone = phone,
                            hometown = request.hometown.trim(),
                            aadharId = request.aadharId.trim(),
                            role = request.role,
                        ),
                    )
                }.getOrNull()
                val syncedSession = remoteResult.session.copy(
                    user = syncedUser ?: remoteResult.session.user.copy(
                        name = request.name.trim(),
                        phone = phone,
                        hometown = request.hometown.trim(),
                        aadharId = request.aadharId.trim(),
                        role = request.role,
                    ),
                )
                _session.value = syncedSession
                return AuthResult.Success(syncedSession)
            }
            return remoteResult
        }

        val user = AuthUser(
            id = "local-${AppStore.accounts.value.size + 1}",
            name = request.name.trim(),
            email = email,
            phone = phone,
            role = request.role,
            hometown = request.hometown.trim(),
            aadharId = request.aadharId.trim(),
        )
        val session = AuthSession(accessToken = "local-${user.id}", user = user)
        AppStore.accounts.value = AppStore.accounts.value + AccountRecord(user = user, password = request.password)
        _session.value = session
        return AuthResult.Success(session)
    }

    override suspend fun signOut() {
        _session.value = null
    }

    override suspend fun updateProfile(request: ProfileUpdateRequest): AuthResult {
        val currentSession = _session.value ?: return AuthResult.Failure(AuthError.UNKNOWN)
        val localUpdatedUser = currentSession.user.copy(
            name = request.name.trim(),
            phone = request.phone.trim(),
            hometown = request.hometown.trim(),
            aadharId = request.aadharId.trim(),
        )
        val updatedUser = if (useSupabase) {
            runCatching {
                supabaseDataGateway.updateUserProfile(
                    sessionToken = currentSession.accessToken,
                    userId = currentSession.user.id,
                    request = request,
                )
            }.getOrNull() ?: localUpdatedUser
        } else {
            localUpdatedUser
        }
        AppStore.accounts.value = AppStore.accounts.value.map { record ->
            if (record.user.id == updatedUser.id) record.copy(user = updatedUser) else record
        }
        val updatedSession = currentSession.copy(user = updatedUser)
        _session.value = updatedSession
        return AuthResult.Success(updatedSession)
    }

    override suspend fun findTenantByPhone(phone: String): AuthUser? {
        if (useSupabase) {
            runCatching {
                supabaseDataGateway.fetchTenantUserByPhone(
                    sessionToken = _session.value?.accessToken,
                    phone = phone,
                )
            }.getOrNull()?.let { return it }
        }
        return AppStore.accounts.value.firstOrNull {
            it.user.role.name == "Tenant" && normalizePhone(it.user.phone) == normalizePhone(phone)
        }?.user
    }

    override suspend fun linkTenantRecord(userId: String, tenantId: String) {
        AppStore.accounts.value = AppStore.accounts.value.map { record ->
            if (record.user.id == userId) {
                val updatedUser = record.user.copy(
                    linkedTenantIds = (record.user.linkedTenantIds + tenantId).distinct(),
                )
                if (_session.value?.user?.id == userId) {
                    _session.value = _session.value?.copy(user = updatedUser)
                }
                record.copy(user = updatedUser)
            } else {
                record
            }
        }
    }

    private suspend fun attemptRemote(block: suspend () -> AuthResult): AuthResult? {
        if (!useSupabase) return null
        return block().let { result ->
            if (result is AuthResult.Failure && result.error == AuthError.BACKEND_NOT_CONFIGURED) null else result
        }
    }

    private suspend fun syncRemoteSession(session: AuthSession): AuthSession {
        val remoteUser = runCatching {
            supabaseDataGateway.fetchUserProfile(
                sessionToken = session.accessToken,
                userId = session.user.id,
            )
        }.getOrNull()

        return if (remoteUser != null) {
            session.copy(user = remoteUser)
        } else {
            val fallbackUser = runCatching {
                supabaseDataGateway.upsertUserProfile(
                    sessionToken = session.accessToken,
                    user = session.user,
                )
            }.getOrNull()
            session.copy(user = fallbackUser ?: session.user)
        }
    }

}
