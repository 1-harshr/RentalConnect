package com.harsh.rentalconnect.data.repository

import com.harsh.rentalconnect.data.MockDataSource
import com.harsh.rentalconnect.domain.model.Property
import com.harsh.rentalconnect.domain.model.Tenant
import com.harsh.rentalconnect.domain.repository.PropertyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class PropertyRepositoryImpl : PropertyRepository {

    override fun getProperties(): Flow<List<Property>> =
        flowOf(MockDataSource.properties)

    override fun getPropertyById(id: String): Flow<Property?> =
        flowOf(MockDataSource.properties.find { it.id == id })

    override fun getTenantsForProperty(propertyId: String): Flow<List<Tenant>> =
        flowOf(MockDataSource.tenants.filter { it.propertyId == propertyId })
}
