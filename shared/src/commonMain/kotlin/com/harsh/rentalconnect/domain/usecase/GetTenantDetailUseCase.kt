package com.harsh.rentalconnect.domain.usecase

import com.harsh.rentalconnect.domain.model.Tenant
import com.harsh.rentalconnect.domain.repository.TenantRepository
import kotlinx.coroutines.flow.Flow

class GetTenantDetailUseCase(private val repository: TenantRepository) {
    operator fun invoke(tenantId: String): Flow<Tenant?> = repository.getTenantById(tenantId)
}
