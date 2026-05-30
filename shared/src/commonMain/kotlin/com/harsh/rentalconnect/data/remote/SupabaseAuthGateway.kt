package com.harsh.rentalconnect.data.remote

import com.harsh.rentalconnect.domain.model.AuthError
import com.harsh.rentalconnect.domain.model.AuthResult
import com.harsh.rentalconnect.domain.model.AuthSession
import com.harsh.rentalconnect.domain.model.AuthUser
import com.harsh.rentalconnect.domain.model.SignInRequest
import com.harsh.rentalconnect.domain.model.SignUpRequest
import com.harsh.rentalconnect.ui.models.Role
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class SupabaseConfig(
    val url: String = "",
    val anonKey: String = "",
) {
    val isConfigured: Boolean
        get() = url.isNotBlank() && anonKey.isNotBlank()
}

class SupabaseAuthGateway(
    private val httpClient: HttpClient,
    private val config: SupabaseConfig,
) {
    suspend fun signIn(request: SignInRequest): AuthResult {
        if (!config.isConfigured) return AuthResult.Failure(AuthError.BACKEND_NOT_CONFIGURED)

        return runCatching {
            val httpResponse: HttpResponse = httpClient.post("${config.url}/auth/v1/token?grant_type=password") {
                contentType(ContentType.Application.Json)
                header("apikey", config.anonKey)
                header(HttpHeaders.Authorization, "Bearer ${config.anonKey}")
                setBody(SignInBody(email = request.email.trim(), password = request.password))
            }
            if (httpResponse.status.isSuccess()) {
                AuthResult.Success(httpResponse.body<SupabaseAuthResponse>().toSession())
            } else {
                AuthResult.Failure(httpResponse.toAuthError())
            }
        }.getOrElse(::mapExceptionToFailure)
    }

    suspend fun signUp(request: SignUpRequest): AuthResult {
        if (!config.isConfigured) return AuthResult.Failure(AuthError.BACKEND_NOT_CONFIGURED)

        return runCatching {
            val httpResponse: HttpResponse = httpClient.post("${config.url}/auth/v1/signup") {
                contentType(ContentType.Application.Json)
                header("apikey", config.anonKey)
                header(HttpHeaders.Authorization, "Bearer ${config.anonKey}")
                setBody(
                    SignUpBody(
                        email = request.email.trim(),
                        password = request.password,
                        data = UserMetadata(
                            name = request.name.trim(),
                            phone = request.phone.trim(),
                            hometown = request.hometown.trim(),
                            aadharId = request.aadharId.trim(),
                            role = request.role.name.lowercase(),
                        ),
                    )
                )
            }
            if (httpResponse.status.isSuccess()) {
                AuthResult.Success(httpResponse.body<SupabaseAuthResponse>().toSession())
            } else {
                AuthResult.Failure(httpResponse.toAuthError())
            }
        }.getOrElse(::mapExceptionToFailure)
    }

    private suspend fun HttpResponse.toAuthError(): AuthError {
        val errorBody = runCatching { body<SupabaseErrorBody>() }.getOrNull()
        val msg = (errorBody?.msg ?: errorBody?.errorDescription ?: "").lowercase()
        val code = errorBody?.errorCode ?: errorBody?.error ?: ""
        return when {
            "invalid_credentials" in code || "invalid login credentials" in msg -> AuthError.INVALID_CREDENTIALS
            "user_already_exists" in code || "already registered" in msg -> AuthError.EMAIL_ALREADY_IN_USE
            else -> AuthError.NETWORK
        }
    }

    private fun SupabaseAuthResponse.toSession(): AuthSession {
        val metadata = user.metadata
        return AuthSession(
            accessToken = accessToken,
            user = AuthUser(
                id = user.id,
                name = metadata?.name ?: user.email.substringBefore('@'),
                email = user.email,
                phone = metadata?.phone.orEmpty(),
                role = if (metadata?.role.equals("tenant", ignoreCase = true)) Role.Tenant else Role.Owner,
                hometown = metadata?.hometown.orEmpty(),
                aadharId = metadata?.aadharId.orEmpty(),
            ),
        )
    }

    private fun mapExceptionToFailure(error: Throwable): AuthResult.Failure {
        val message = error.message.orEmpty().lowercase()
        val authError = when {
            "invalid login credentials" in message -> AuthError.INVALID_CREDENTIALS
            "already registered" in message -> AuthError.EMAIL_ALREADY_IN_USE
            else -> AuthError.NETWORK
        }
        return AuthResult.Failure(authError)
    }
}

@Serializable
private data class SignInBody(
    val email: String,
    val password: String,
)

@Serializable
private data class SignUpBody(
    val email: String,
    val password: String,
    val data: UserMetadata,
)

@Serializable
private data class UserMetadata(
    val name: String,
    val phone: String,
    val hometown: String = "",
    @SerialName("aadhar_id") val aadharId: String = "",
    val role: String,
)

@Serializable
private data class SupabaseAuthResponse(
    @SerialName("access_token") val accessToken: String,
    val user: SupabaseUser,
)

@Serializable
private data class SupabaseUser(
    val id: String,
    val email: String,
    @SerialName("user_metadata") val metadata: UserMetadata? = null,
)

@Serializable
private data class SupabaseErrorBody(
    @SerialName("error_code") val errorCode: String? = null,
    @SerialName("msg") val msg: String? = null,
    val error: String? = null,
    @SerialName("error_description") val errorDescription: String? = null,
)
