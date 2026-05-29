package com.harsh.rentalconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.rentalconnect.domain.model.Tenant
import com.harsh.rentalconnect.domain.usecase.GetPropertyDetailUseCase
import com.harsh.rentalconnect.ui.models.PropertyDetail
import com.harsh.rentalconnect.ui.models.TenantSummary
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class PropertyDetailOwnerUiState {
    object Loading : PropertyDetailOwnerUiState()
    data class Success(val property: PropertyDetail) : PropertyDetailOwnerUiState()
    data class Error(val message: String) : PropertyDetailOwnerUiState()
}

class PropertyDetailOwnerViewModel(
    propertyId: String,
    getPropertyDetail: GetPropertyDetailUseCase,
) : ViewModel() {

    val uiState: StateFlow<PropertyDetailOwnerUiState> = getPropertyDetail(propertyId)
        .map<_, PropertyDetailOwnerUiState> { (property, tenants) ->
            if (property == null) {
                PropertyDetailOwnerUiState.Error("Property not found")
            } else {
                PropertyDetailOwnerUiState.Success(
                    PropertyDetail(
                        id = property.id,
                        name = property.name,
                        address = property.address,
                        type = property.type,
                        houseNumber = property.houseNumber,
                        isOccupied = property.isOccupied,
                        tenants = tenants.map { it.toSummary() },
                    )
                )
            }
        }
        .catch { emit(PropertyDetailOwnerUiState.Error(it.message ?: "Unknown error")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PropertyDetailOwnerUiState.Loading)

    private fun Tenant.toSummary() = TenantSummary(
        id = id,
        name = name,
        initials = initials(name),
        flatNumber = flatNumber,
        since = since,
    )
}
