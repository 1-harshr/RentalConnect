package com.harsh.rentalconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddPropertyUiState(
    val propertyName: String = "",
    val houseNumber: String = "",
    val fullAddress: String = "",
    val type: String = "",
    val isOccupied: Boolean = true,
    val isSaving: Boolean = false,
)

class AddPropertyViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddPropertyUiState())
    val uiState: StateFlow<AddPropertyUiState> = _uiState.asStateFlow()

    fun onPropertyNameChange(value: String) = _uiState.update { it.copy(propertyName = value) }
    fun onHouseNumberChange(value: String) = _uiState.update { it.copy(houseNumber = value) }
    fun onFullAddressChange(value: String) = _uiState.update { it.copy(fullAddress = value) }
    fun onTypeChange(value: String) = _uiState.update { it.copy(type = value) }
    fun onAvailabilityChange(isOccupied: Boolean) = _uiState.update { it.copy(isOccupied = isOccupied) }

    fun save(onSuccess: () -> Unit) {
        // TODO: persist via repository
        onSuccess()
    }
}
