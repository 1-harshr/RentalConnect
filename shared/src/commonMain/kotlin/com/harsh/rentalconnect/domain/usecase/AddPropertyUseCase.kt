package com.harsh.rentalconnect.domain.usecase

import com.harsh.rentalconnect.domain.model.AddPropertyDraft
import com.harsh.rentalconnect.domain.model.Property
import com.harsh.rentalconnect.domain.repository.PropertyRepository

class AddPropertyUseCase(
    private val propertyRepository: PropertyRepository,
) {
    suspend operator fun invoke(
        draft: AddPropertyDraft,
        ownerId: String,
        ownerName: String,
        ownerPhone: String,
    ): Property = propertyRepository.addProperty(
        draft = draft,
        ownerId = ownerId,
        ownerName = ownerName,
        ownerPhone = ownerPhone,
    )
}
