package com.harsh.rentalconnect.domain.repository

import com.harsh.rentalconnect.domain.model.Property
import com.harsh.rentalconnect.domain.model.Tenant
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {
    fun getProperties(): Flow<List<Property>>
    fun getPropertyById(id: String): Flow<Property?>
    fun getTenantsForProperty(propertyId: String): Flow<List<Tenant>>
}
