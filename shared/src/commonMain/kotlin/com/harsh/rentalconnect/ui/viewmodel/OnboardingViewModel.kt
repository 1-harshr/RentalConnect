package com.harsh.rentalconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.rentalconnect.domain.model.AuthError
import com.harsh.rentalconnect.domain.model.AuthResult
import com.harsh.rentalconnect.domain.model.AuthValidationError
import com.harsh.rentalconnect.domain.model.ValidationError
import com.harsh.rentalconnect.domain.model.ValidationResult
import com.harsh.rentalconnect.domain.model.SignUpRequest
import com.harsh.rentalconnect.domain.repository.AuthRepository
import com.harsh.rentalconnect.domain.usecase.ValidateAadharUseCase
import com.harsh.rentalconnect.domain.usecase.ValidateEmailUseCase
import com.harsh.rentalconnect.domain.usecase.ValidateNameUseCase
import com.harsh.rentalconnect.domain.usecase.ValidatePasswordUseCase
import com.harsh.rentalconnect.domain.usecase.ValidatePhoneUseCase
import com.harsh.rentalconnect.ui.components.CountryCode
import com.harsh.rentalconnect.ui.components.countryCodes
import com.harsh.rentalconnect.ui.models.Role
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OnboardingUiState(
    val name: String = "",
    val phone: String = "",
    val selectedCountryCode: CountryCode = countryCodes.first(),
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val hometown: String = "",
    val aadharId: String = "",
    val selectedRole: Role = Role.Owner,
    val nameError: ValidationError? = null,
    val phoneError: ValidationError? = null,
    val emailError: AuthValidationError? = null,
    val passwordError: AuthValidationError? = null,
    val confirmPasswordError: AuthValidationError? = null,
    val hometownError: ValidationError? = null,
    val aadharError: AuthValidationError? = null,
    val authError: AuthError? = null,
    val isSubmitting: Boolean = false,
) {
    val canContinue: Boolean
        get() = name.isNotBlank() &&
            phone.isNotBlank() &&
            email.isNotBlank() &&
            password.isNotBlank() &&
            confirmPassword.isNotBlank() &&
            hometown.isNotBlank() &&
            aadharId.isNotBlank() &&
            nameError == null &&
            phoneError == null &&
            emailError == null &&
            passwordError == null &&
            confirmPasswordError == null &&
            hometownError == null &&
            aadharError == null
}

class OnboardingViewModel(
    private val validateName: ValidateNameUseCase,
    private val validatePhone: ValidatePhoneUseCase,
    private val validateEmail: ValidateEmailUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val validateAadhar: ValidateAadharUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        val error = if (name.isNotEmpty()) (validateName(name) as? ValidationResult.Invalid)?.error else null
        _uiState.update { it.copy(name = name, nameError = error, authError = null) }
    }

    fun onPhoneChange(phone: String) {
        val dialCode = _uiState.value.selectedCountryCode.dialCode
        val error = if (phone.isNotEmpty()) (validatePhone(phone, dialCode) as? ValidationResult.Invalid)?.error else null
        _uiState.update { it.copy(phone = phone, phoneError = error, authError = null) }
    }

    fun onCountryCodeChange(countryCode: CountryCode) {
        val error = if (_uiState.value.phone.isNotEmpty()) {
            (validatePhone(_uiState.value.phone, countryCode.dialCode) as? ValidationResult.Invalid)?.error
        } else null
        _uiState.update { it.copy(selectedCountryCode = countryCode, phoneError = error, authError = null) }
    }

    fun onEmailChange(email: String) {
        val error = if (email.isNotEmpty()) validateEmail(email) else null
        _uiState.update { it.copy(email = email, emailError = error, authError = null) }
    }

    fun onPasswordChange(password: String) {
        val passwordError = if (password.isNotEmpty()) validatePassword(password) else null
        val confirmPasswordError = confirmPasswordError(password, _uiState.value.confirmPassword)
        _uiState.update {
            it.copy(
                password = password,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError,
                authError = null,
            )
        }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update {
            it.copy(
                confirmPassword = confirmPassword,
                confirmPasswordError = confirmPasswordError(it.password, confirmPassword),
                authError = null,
            )
        }
    }

    fun onRoleSelected(role: Role) {
        _uiState.update { it.copy(selectedRole = role) }
    }

    fun onHometownChange(hometown: String) {
        _uiState.update {
            it.copy(
                hometown = hometown,
                hometownError = if (hometown.isBlank()) null else hometownRequiredError(hometown),
                authError = null,
            )
        }
    }

    fun onAadharChange(aadharId: String) {
        _uiState.update {
            it.copy(
                aadharId = aadharId,
                aadharError = if (aadharId.isBlank()) null else validateAadhar(aadharId),
                authError = null,
            )
        }
    }

    fun submit() {
        val nameError = (validateName(_uiState.value.name) as? ValidationResult.Invalid)?.error
        val phoneError = (validatePhone(_uiState.value.phone, _uiState.value.selectedCountryCode.dialCode) as? ValidationResult.Invalid)?.error
        val emailError = validateEmail(_uiState.value.email)
        val passwordError = validatePassword(_uiState.value.password)
        val confirmPasswordError = confirmPasswordError(_uiState.value.password, _uiState.value.confirmPassword)
        val hometownError = hometownRequiredError(_uiState.value.hometown)
        val aadharError = validateAadhar(_uiState.value.aadharId)
        val hasError = nameError != null ||
            phoneError != null ||
            emailError != null ||
            passwordError != null ||
            confirmPasswordError != null ||
            hometownError != null ||
            aadharError != null

        _uiState.update {
            it.copy(
                nameError = nameError,
                phoneError = phoneError,
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError,
                hometownError = hometownError,
                aadharError = aadharError,
            )
        }
        if (hasError) return

        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, authError = null) }
            when (
                val result = authRepository.signUp(
                    SignUpRequest(
                        name = state.name,
                        email = state.email,
                        phone = "${state.selectedCountryCode.dialCode}${state.phone}",
                        hometown = state.hometown,
                        aadharId = state.aadharId,
                        password = state.password,
                        role = state.selectedRole,
                    )
                )
            ) {
                is AuthResult.Success -> _uiState.update { current ->
                    current.copy(isSubmitting = false, authError = null)
                }
                is AuthResult.Failure -> _uiState.update { current ->
                    current.copy(isSubmitting = false, authError = result.error)
                }
            }
        }
    }

    private fun confirmPasswordError(password: String, confirmPassword: String): AuthValidationError? = when {
        confirmPassword.isBlank() -> null
        password != confirmPassword -> AuthValidationError.MISMATCH
        else -> null
    }

    private fun hometownRequiredError(hometown: String): ValidationError? =
        if (hometown.isBlank()) ValidationError.EMPTY else null
}
