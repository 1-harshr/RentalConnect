package com.harsh.rentalconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.rentalconnect.domain.model.AuthUser
import com.harsh.rentalconnect.domain.repository.AuthRepository
import com.harsh.rentalconnect.domain.repository.TenantRepository
import com.harsh.rentalconnect.domain.usecase.ValidatePhoneUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddTenantUiState(
    val phone: String = "",
    val phoneError: String? = null,
    val candidate: AuthUser? = null,
    val searchError: String? = null,
    val isSearching: Boolean = false,
    val isConfirming: Boolean = false,
)

class AddTenantViewModel(
    private val propertyId: String,
    private val authRepository: AuthRepository,
    private val tenantRepository: TenantRepository,
    private val validatePhone: ValidatePhoneUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddTenantUiState())
    val uiState: StateFlow<AddTenantUiState> = _uiState.asStateFlow()

    fun onPhoneChange(value: String) {
        _uiState.update { it.copy(phone = value, phoneError = null, searchError = null, candidate = null) }
    }

    fun search() {
        val phone = _uiState.value.phone
        val valid = validatePhone(phone)
        if (valid !is com.harsh.rentalconnect.domain.model.ValidationResult.Valid) {
            _uiState.update { it.copy(phoneError = "Enter a valid 10-digit phone number") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSearching = true, searchError = null, candidate = null) }
            val candidate = authRepository.findTenantByPhone(phone)
            if (candidate == null) {
                _uiState.update { it.copy(isSearching = false, searchError = "No tenant account found for that phone number.") }
                return@launch
            }
            val existing = tenantRepository.getTenantsForPhone(candidate.phone).first().any { it.propertyId == propertyId }
            if (existing) {
                _uiState.update { it.copy(isSearching = false, searchError = "This tenant is already assigned to the property.") }
                return@launch
            }
            _uiState.update { it.copy(isSearching = false, candidate = candidate) }
        }
    }

    fun confirm(onSuccess: () -> Unit) {
        val candidate = _uiState.value.candidate ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isConfirming = true) }
            val tenant = tenantRepository.addTenantToProperty(
                propertyId = propertyId,
                tenantName = candidate.name,
                tenantPhone = candidate.phone,
                hometown = candidate.hometown,
                aadharId = candidate.aadharId,
            )
            authRepository.linkTenantRecord(candidate.id, tenant.id)
            _uiState.update { it.copy(isConfirming = false) }
            onSuccess()
        }
    }
}
