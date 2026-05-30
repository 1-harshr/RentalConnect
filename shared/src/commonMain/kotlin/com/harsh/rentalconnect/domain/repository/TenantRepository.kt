package com.harsh.rentalconnect.domain.repository

import com.harsh.rentalconnect.domain.model.Tenant
import kotlinx.coroutines.flow.Flow

interface TenantRepository {
    fun getTenantById(id: String): Flow<Tenant?>
    fun getTenantsForPhone(phone: String): Flow<List<Tenant>>
    suspend fun refreshTenantsForPhone(phone: String)
    suspend fun refreshTenant(tenantId: String)
    suspend fun addTenantToProperty(propertyId: String, tenantName: String, tenantPhone: String, hometown: String, aadharId: String): Tenant
    suspend fun removeTenant(tenantId: String)
}
