package com.harsh.rentalconnect.domain.usecase

import com.harsh.rentalconnect.domain.model.Tenant
import com.harsh.rentalconnect.domain.repository.TenantRepository
import kotlinx.coroutines.flow.Flow

class GetTenantAssignmentsUseCase(
    private val repository: TenantRepository,
) {
    operator fun invoke(phone: String): Flow<List<Tenant>> = repository.getTenantsForPhone(phone)
}
