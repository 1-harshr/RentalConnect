package com.harsh.rentalconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.harsh.rentalconnect.domain.model.ValidationError
import com.harsh.rentalconnect.domain.model.ValidationResult
import com.harsh.rentalconnect.domain.usecase.ValidateNameUseCase
import com.harsh.rentalconnect.domain.usecase.ValidatePhoneUseCase
import com.harsh.rentalconnect.ui.models.Role
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class OnboardingUiState(
    val name: String = "",
    val phone: String = "",
    val selectedRole: Role = Role.Owner,
    val nameError: ValidationError? = null,
    val phoneError: ValidationError? = null,
) {
    // True only when both fields are filled and have no errors
    val canContinue: Boolean
        get() = name.isNotBlank() && phone.isNotBlank() && nameError == null && phoneError == null
}

class OnboardingViewModel(
    private val validateName: ValidateNameUseCase = ValidateNameUseCase(),
    private val validatePhone: ValidatePhoneUseCase = ValidatePhoneUseCase(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        // Only show error while typing if the field is non-empty
        val error = if (name.isNotEmpty()) (validateName(name) as? ValidationResult.Invalid)?.error else null
        _uiState.update { it.copy(name = name, nameError = error) }
    }

    fun onPhoneChange(phone: String) {
        // Only show error while typing if the field is non-empty
        val error = if (phone.isNotEmpty()) (validatePhone(phone) as? ValidationResult.Invalid)?.error else null
        _uiState.update { it.copy(phone = phone, phoneError = error) }
    }

    fun onRoleSelected(role: Role) {
        _uiState.update { it.copy(selectedRole = role) }
    }

    // Called when user taps Continue. Forces validation on all fields (including empty ones)
    // and returns true only if the form is fully valid.
    fun onContinueAttempted(): Boolean {
        val nameError = (validateName(_uiState.value.name) as? ValidationResult.Invalid)?.error
        val phoneError = (validatePhone(_uiState.value.phone) as? ValidationResult.Invalid)?.error
        _uiState.update { it.copy(nameError = nameError, phoneError = phoneError) }
        return nameError == null && phoneError == null
    }
}
