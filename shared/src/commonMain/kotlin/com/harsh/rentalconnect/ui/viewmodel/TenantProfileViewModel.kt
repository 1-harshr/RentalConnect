package com.harsh.rentalconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.rentalconnect.domain.usecase.GetTenantDetailUseCase
import com.harsh.rentalconnect.ui.models.TenantDetail
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class TenantProfileUiState {
    object Loading : TenantProfileUiState()
    data class Success(val tenant: TenantDetail) : TenantProfileUiState()
    data class Error(val message: String) : TenantProfileUiState()
}

class TenantProfileViewModel(
    tenantId: String,
    getTenantDetail: GetTenantDetailUseCase,
) : ViewModel() {

    val uiState: StateFlow<TenantProfileUiState> = getTenantDetail(tenantId)
        .map<_, TenantProfileUiState> { tenant ->
            if (tenant == null) {
                TenantProfileUiState.Error("Tenant not found")
            } else {
                TenantProfileUiState.Success(
                    TenantDetail(
                        id = tenant.id,
                        name = tenant.name,
                        initials = initials(tenant.name),
                        propertyName = tenant.propertyName,
                        flatNumber = tenant.flatNumber,
                        phone = tenant.phone,
                        hometown = tenant.hometown,
                        aadharId = tenant.aadharId,
                        tenantSince = tenant.since,
                        assignedPropertyName = tenant.propertyName,
                        assignedPropertyAddress = tenant.propertyAddress,
                    )
                )
            }
        }
        .catch { emit(TenantProfileUiState.Error(it.message ?: "Unknown error")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TenantProfileUiState.Loading)
}
