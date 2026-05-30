package com.harsh.rentalconnect.domain.usecase

import com.harsh.rentalconnect.domain.model.AddPropertyDraft
import com.harsh.rentalconnect.domain.model.Property
import com.harsh.rentalconnect.domain.repository.PropertyRepository

class UpdatePropertyUseCase(
    private val propertyRepository: PropertyRepository,
) {
    suspend operator fun invoke(
        propertyId: String,
        draft: AddPropertyDraft,
    ): Property? = propertyRepository.updateProperty(
        propertyId = propertyId,
        draft = draft,
    )
}
