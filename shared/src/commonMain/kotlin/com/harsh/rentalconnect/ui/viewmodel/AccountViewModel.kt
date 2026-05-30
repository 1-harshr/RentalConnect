package com.harsh.rentalconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.rentalconnect.domain.model.AuthResult
import com.harsh.rentalconnect.domain.model.AuthSession
import com.harsh.rentalconnect.domain.model.AuthValidationError
import com.harsh.rentalconnect.domain.model.ProfileUpdateRequest
import com.harsh.rentalconnect.domain.model.ValidationError
import com.harsh.rentalconnect.domain.model.ValidationResult
import com.harsh.rentalconnect.domain.repository.AuthRepository
import com.harsh.rentalconnect.domain.usecase.ValidateAadharUseCase
import com.harsh.rentalconnect.domain.usecase.ValidateNameUseCase
import com.harsh.rentalconnect.domain.usecase.ValidatePhoneUseCase
import com.harsh.rentalconnect.ui.components.CountryCode
import com.harsh.rentalconnect.ui.components.countryCodes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AccountUiState(
    val session: AuthSession? = null,
    val draftName: String = "",
    val draftPhone: String = "",
    val draftHometown: String = "",
    val draftAadharId: String = "",
    val selectedCountryCode: CountryCode = countryCodes.first(),
    val nameError: ValidationError? = null,
    val phoneError: ValidationError? = null,
    val hometownError: ValidationError? = null,
    val aadharError: AuthValidationError? = null,
    val saveError: AuthResult.Failure? = null,
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
)

class AccountViewModel(
    private val authRepository: AuthRepository,
    private val validateName: ValidateNameUseCase,
    private val validatePhone: ValidatePhoneUseCase,
    private val validateAadhar: ValidateAadharUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AccountUiState(session = authRepository.session.value))
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.session.collect { session ->
                _uiState.update { current ->
                    current.copy(
                        session = session,
                        draftName = if (current.isEditing) current.draftName else session?.user?.name.orEmpty(),
                        draftPhone = if (current.isEditing) current.draftPhone else session?.user?.phone.orEmpty(),
                        draftHometown = if (current.isEditing) current.draftHometown else session?.user?.hometown.orEmpty(),
                        draftAadharId = if (current.isEditing) current.draftAadharId else session?.user?.aadharId.orEmpty(),
                    )
                }
            }
        }
    }

    fun startEditing() {
        val user = _uiState.value.session?.user ?: return
        val detected = countryCodes.firstOrNull { user.phone.startsWith(it.dialCode) } ?: countryCodes.first()
        val localNumber = user.phone.removePrefix(detected.dialCode)
        _uiState.update {
            it.copy(
                draftName = user.name,
                draftPhone = localNumber,
                draftHometown = user.hometown,
                draftAadharId = user.aadharId,
                selectedCountryCode = detected,
                isEditing = true,
                nameError = null,
                phoneError = null,
                hometownError = null,
                aadharError = null,
                saveError = null,
            )
        }
    }

    fun cancelEditing() {
        _uiState.update { state ->
            val user = state.session?.user
            state.copy(
                draftName = user?.name.orEmpty(),
                draftPhone = user?.phone.orEmpty(),
                draftHometown = user?.hometown.orEmpty(),
                draftAadharId = user?.aadharId.orEmpty(),
                isEditing = false,
                nameError = null,
                phoneError = null,
                hometownError = null,
                aadharError = null,
                saveError = null,
            )
        }
    }

    fun onNameChange(value: String) {
        _uiState.update { it.copy(draftName = value, nameError = null, saveError = null) }
    }

    fun onPhoneChange(value: String) {
        val dialCode = _uiState.value.selectedCountryCode.dialCode
        val error = if (value.isNotEmpty()) (validatePhone(value, dialCode) as? ValidationResult.Invalid)?.error else null
        _uiState.update { it.copy(draftPhone = value, phoneError = error, saveError = null) }
    }

    fun onCountryCodeChange(countryCode: CountryCode) {
        val error = if (_uiState.value.draftPhone.isNotEmpty()) {
            (validatePhone(_uiState.value.draftPhone, countryCode.dialCode) as? ValidationResult.Invalid)?.error
        } else null
        _uiState.update { it.copy(selectedCountryCode = countryCode, phoneError = error, saveError = null) }
    }

    fun onHometownChange(value: String) {
        _uiState.update { it.copy(draftHometown = value, hometownError = null, saveError = null) }
    }

    fun onAadharChange(value: String) {
        _uiState.update { it.copy(draftAadharId = value, aadharError = null, saveError = null) }
    }

    fun save() {
        val state = _uiState.value
        val nameError = (validateName(state.draftName) as? ValidationResult.Invalid)?.error
        val phoneError = (validatePhone(state.draftPhone, state.selectedCountryCode.dialCode) as? ValidationResult.Invalid)?.error
        val hometownError = if (state.draftHometown.isBlank()) ValidationError.EMPTY else null
        val aadharError = validateAadhar(state.draftAadharId)

        _uiState.update {
            it.copy(
                nameError = nameError,
                phoneError = phoneError,
                hometownError = hometownError,
                aadharError = aadharError,
                saveError = null,
            )
        }
        if (nameError != null || phoneError != null || hometownError != null || aadharError != null) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            when (val result = authRepository.updateProfile(
                    ProfileUpdateRequest(
                        name = state.draftName,
                        phone = "${state.selectedCountryCode.dialCode}${state.draftPhone}",
                        hometown = state.draftHometown,
                        aadharId = state.draftAadharId,
                    )
                )) {
                is AuthResult.Success -> _uiState.update { it.copy(isEditing = false, isSaving = false, saveError = null) }
                is AuthResult.Failure -> _uiState.update { it.copy(isSaving = false, saveError = result) }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }
}
