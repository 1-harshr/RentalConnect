package com.harsh.rentalconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.rentalconnect.domain.usecase.GetPropertiesUseCase
import com.harsh.rentalconnect.ui.models.OwnerStats
import com.harsh.rentalconnect.ui.models.PropertySummary
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class OwnerHomeUiState {
    object Loading : OwnerHomeUiState()
    data class Success(
        val userName: String,
        val userInitials: String,
        val stats: OwnerStats,
        val properties: List<PropertySummary>,
    ) : OwnerHomeUiState()
    data class Error(val message: String) : OwnerHomeUiState()
}

class OwnerHomeViewModel(
    getProperties: GetPropertiesUseCase,
    private val ownerName: String = "Rajesh Kumar",
) : ViewModel() {

    val uiState: StateFlow<OwnerHomeUiState> = getProperties()
        .map<_, OwnerHomeUiState> { properties ->
            OwnerHomeUiState.Success(
                userName = ownerName,
                userInitials = initials(ownerName),
                stats = OwnerStats(
                    propertyCount = properties.size,
                    tenantCount = properties.sumOf { it.tenantCount },
                    vacantCount = properties.count { !it.isOccupied },
                ),
                properties = properties.map { p ->
                    PropertySummary(
                        id = p.id,
                        name = p.name,
                        address = p.address,
                        tenantCount = p.tenantCount,
                        type = p.type,
                        isOccupied = p.isOccupied,
                    )
                },
            )
        }
        .catch { emit(OwnerHomeUiState.Error(it.message ?: "Unknown error")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), OwnerHomeUiState.Loading)
}

internal fun initials(name: String): String =
    name.split(" ").filter { it.isNotEmpty() }.take(2)
        .joinToString("") { it.first().uppercaseChar().toString() }
