package com.harsh.rentalconnect.domain.repository

import com.harsh.rentalconnect.domain.model.Tenant
import kotlinx.coroutines.flow.Flow

interface TenantRepository {
    fun getTenantById(id: String): Flow<Tenant?>
}
