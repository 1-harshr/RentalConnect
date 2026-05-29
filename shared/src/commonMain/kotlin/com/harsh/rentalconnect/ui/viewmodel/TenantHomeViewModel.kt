package com.harsh.rentalconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.rentalconnect.domain.usecase.GetPropertyDetailUseCase
import com.harsh.rentalconnect.domain.usecase.GetTenantDetailUseCase
import com.harsh.rentalconnect.ui.models.OwnerInfo
import com.harsh.rentalconnect.ui.models.Role
import com.harsh.rentalconnect.ui.models.TenantPropertyInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class TenantHomeUiState {
    object Loading : TenantHomeUiState()
    data class Success(
        val userName: String,
        val userInitials: String,
        val property: TenantPropertyInfo,
        val owner: OwnerInfo,
    ) : TenantHomeUiState()
    data class Error(val message: String) : TenantHomeUiState()
}

class TenantHomeViewModel(
    tenantId: String = "t1",
    getTenantDetail: GetTenantDetailUseCase,
    getPropertyDetail: GetPropertyDetailUseCase,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<TenantHomeUiState> = getTenantDetail(tenantId)
        .flatMapLatest { tenant ->
            if (tenant == null) return@flatMapLatest flowOf(TenantHomeUiState.Error("Tenant not found"))
            getPropertyDetail(tenant.propertyId).map<_, TenantHomeUiState> { (property, _) ->
                if (property == null) {
                    TenantHomeUiState.Error("Property not found")
                } else {
                    TenantHomeUiState.Success(
                        userName = tenant.name,
                        userInitials = initials(tenant.name),
                        property = TenantPropertyInfo(
                            propertyName = property.name,
                            address = property.address,
                            flatNumber = tenant.flatNumber,
                            type = property.type,
                        ),
                        owner = OwnerInfo(
                            name = property.ownerName,
                            initials = initials(property.ownerName),
                            role = Role.Owner,
                        ),
                    )
                }
            }
        }
        .catch { emit(TenantHomeUiState.Error(it.message ?: "Unknown error")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TenantHomeUiState.Loading)
}
