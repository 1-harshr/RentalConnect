package com.harsh.rentalconnect.domain.usecase

import com.harsh.rentalconnect.domain.model.Property
import com.harsh.rentalconnect.domain.repository.PropertyRepository
import kotlinx.coroutines.flow.Flow

class GetPropertiesUseCase(private val repository: PropertyRepository) {
    operator fun invoke(ownerId: String? = null): Flow<List<Property>> = repository.getProperties(ownerId)
}
