package com.harsh.rentalconnect.domain.usecase

import com.harsh.rentalconnect.domain.model.Property
import com.harsh.rentalconnect.domain.model.Tenant
import com.harsh.rentalconnect.domain.repository.PropertyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetPropertyDetailUseCase(private val repository: PropertyRepository) {
    operator fun invoke(propertyId: String): Flow<Pair<Property?, List<Tenant>>> =
        combine(
            repository.getPropertyById(propertyId),
            repository.getTenantsForProperty(propertyId),
        ) { property, tenants -> property to tenants }
}
