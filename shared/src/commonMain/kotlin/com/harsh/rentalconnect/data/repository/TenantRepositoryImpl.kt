package com.harsh.rentalconnect.data.repository

import com.harsh.rentalconnect.data.MockDataSource
import com.harsh.rentalconnect.domain.model.Tenant
import com.harsh.rentalconnect.domain.repository.TenantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class TenantRepositoryImpl : TenantRepository {

    override fun getTenantById(id: String): Flow<Tenant?> =
        flowOf(MockDataSource.tenants.find { it.id == id })
}
