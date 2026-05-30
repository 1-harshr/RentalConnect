package com.harsh.rentalconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.rentalconnect.domain.model.AuthUser
import com.harsh.rentalconnect.domain.repository.PropertyRepository
import com.harsh.rentalconnect.domain.repository.TenantRepository
import com.harsh.rentalconnect.domain.usecase.GetPropertyDetailUseCase
import com.harsh.rentalconnect.domain.usecase.GetTenantAssignmentsUseCase
import com.harsh.rentalconnect.ui.models.TenantOwnerInfo
import com.harsh.rentalconnect.ui.models.TenantPropertyDetail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class PropertyDetailTenantUiState {
    object Loading : PropertyDetailTenantUiState()
    data class Success(
        val property: TenantPropertyDetail,
        val owner: TenantOwnerInfo,
    ) : PropertyDetailTenantUiState()
    data class Error(val message: String) : PropertyDetailTenantUiState()
}

class PropertyDetailTenantViewModel(
    propertyId: String,
    currentUser: AuthUser,
    tenantRepository: TenantRepository,
    propertyRepository: PropertyRepository,
    getTenantAssignments: GetTenantAssignmentsUseCase,
    getPropertyDetail: GetPropertyDetailUseCase,
) : ViewModel() {
    init {
        viewModelScope.launch {
            tenantRepository.refreshTenantsForPhone(currentUser.phone)
            propertyRepository.refreshPropertyDetail(propertyId)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<PropertyDetailTenantUiState> = getTenantAssignments(currentUser.phone)
        .flatMapLatest { assignments ->
            val tenant = assignments.firstOrNull { it.propertyId == propertyId }
            if (tenant == null) return@flatMapLatest flowOf(PropertyDetailTenantUiState.Error("Tenant not found"))
            getPropertyDetail(tenant.propertyId).map<_, PropertyDetailTenantUiState> { (property, _) ->
                if (property == null) {
                    PropertyDetailTenantUiState.Error("Property not found")
                } else {
                    PropertyDetailTenantUiState.Success(
                        property = TenantPropertyDetail(
                            propertyName = property.name,
                            address = property.address,
                            flatNumber = tenant.flatNumber,
                            type = property.type,
                            houseNumber = property.houseNumber,
                            photoUrls = property.photoUrls,
                        ),
                        owner = TenantOwnerInfo(
                            name = property.ownerName,
                            initials = initials(property.ownerName),
                            phone = property.ownerPhone,
                        ),
                    )
                }
            }
        }
        .catch { emit(PropertyDetailTenantUiState.Error(it.message ?: "Unknown error")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PropertyDetailTenantUiState.Loading)
}
