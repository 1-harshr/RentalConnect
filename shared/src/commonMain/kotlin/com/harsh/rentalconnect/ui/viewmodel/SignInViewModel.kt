package com.harsh.rentalconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.rentalconnect.domain.model.AuthError
import com.harsh.rentalconnect.domain.model.AuthResult
import com.harsh.rentalconnect.domain.model.AuthValidationError
import com.harsh.rentalconnect.domain.model.SignInRequest
import com.harsh.rentalconnect.domain.repository.AuthRepository
import com.harsh.rentalconnect.domain.usecase.ValidateEmailUseCase
import com.harsh.rentalconnect.domain.usecase.ValidatePasswordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val emailError: AuthValidationError? = null,
    val passwordError: AuthValidationError? = null,
    val authError: AuthError? = null,
    val isSubmitting: Boolean = false,
)

class SignInViewModel(
    private val authRepository: AuthRepository,
    private val validateEmail: ValidateEmailUseCase,
    private val validatePassword: ValidatePasswordUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.update {
            it.copy(
                email = value,
                emailError = if (value.isBlank()) null else validateEmail(value),
                authError = null,
            )
        }
    }

    fun onPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                password = value,
                passwordError = if (value.isBlank()) null else validatePassword(value),
                authError = null,
            )
        }
    }

    fun submit() {
        val state = _uiState.value
        val emailError = validateEmail(state.email)
        val passwordError = validatePassword(state.password)

        if (emailError != null || passwordError != null) {
            _uiState.update { it.copy(emailError = emailError, passwordError = passwordError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, authError = null) }
            when (val result = authRepository.signIn(SignInRequest(state.email, state.password))) {
                is AuthResult.Success -> _uiState.update { current ->
                    current.copy(isSubmitting = false, authError = null)
                }
                is AuthResult.Failure -> _uiState.update { current ->
                    current.copy(isSubmitting = false, authError = result.error)
                }
            }
        }
    }
}
