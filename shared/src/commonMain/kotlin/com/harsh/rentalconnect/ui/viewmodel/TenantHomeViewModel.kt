package com.harsh.rentalconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.rentalconnect.domain.model.AuthUser
import com.harsh.rentalconnect.domain.repository.PropertyRepository
import com.harsh.rentalconnect.domain.repository.TenantRepository
import com.harsh.rentalconnect.domain.usecase.GetPropertiesUseCase
import com.harsh.rentalconnect.domain.usecase.GetTenantAssignmentsUseCase
import com.harsh.rentalconnect.ui.models.TenantPropertyInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class TenantHomeUiState {
    object Loading : TenantHomeUiState()
    data class Empty(val message: String) : TenantHomeUiState()
    data class Success(
        val userName: String,
        val userInitials: String,
        val rentals: List<TenantPropertyInfo>,
    ) : TenantHomeUiState()
    data class Error(val message: String) : TenantHomeUiState()
}

class TenantHomeViewModel(
    currentUser: AuthUser,
    tenantRepository: TenantRepository,
    propertyRepository: PropertyRepository,
    getTenantAssignments: GetTenantAssignmentsUseCase,
    getProperties: GetPropertiesUseCase,
) : ViewModel() {
    init {
        viewModelScope.launch {
            tenantRepository.refreshTenantsForPhone(currentUser.phone)
            propertyRepository.refreshProperties()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<TenantHomeUiState> = combine(
        getTenantAssignments(currentUser.phone),
        getProperties(),
    ) { assignments, properties ->
        val sortedAssignments = assignments.sortedBy { it.propertyName }
        val propertiesById = properties.associateBy { it.id }
        if (sortedAssignments.isEmpty()) {
            TenantHomeUiState.Empty("Your account is ready, but no property has been linked yet.")
        } else {
            val rentals = sortedAssignments.mapNotNull { tenant ->
                val property = propertiesById[tenant.propertyId] ?: return@mapNotNull null
                TenantPropertyInfo(
                    propertyId = property.id,
                    propertyName = property.name,
                    address = property.address,
                    houseNumber = property.houseNumber,
                    flatNumber = tenant.flatNumber,
                    type = property.type,
                    isOccupied = property.isOccupied,
                    ownerName = property.ownerName,
                    ownerPhone = property.ownerPhone,
                )
            }
            TenantHomeUiState.Success(
                userName = currentUser.name,
                userInitials = initials(currentUser.name),
                rentals = rentals,
            )
        }
    }
        .catch { emit(TenantHomeUiState.Error(it.message ?: "Unknown error")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TenantHomeUiState.Loading)
}
