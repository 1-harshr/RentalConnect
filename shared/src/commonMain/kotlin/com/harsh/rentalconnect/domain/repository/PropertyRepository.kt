package com.harsh.rentalconnect.domain.repository

import com.harsh.rentalconnect.domain.model.Property
import com.harsh.rentalconnect.domain.model.Tenant
import com.harsh.rentalconnect.domain.model.AddPropertyDraft
import com.harsh.rentalconnect.domain.model.PickedPhoto
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {
    fun getProperties(ownerId: String? = null): Flow<List<Property>>
    fun getPropertyById(id: String): Flow<Property?>
    fun getTenantsForProperty(propertyId: String): Flow<List<Tenant>>
    suspend fun refreshProperties(ownerId: String? = null)
    suspend fun refreshPropertyDetail(propertyId: String)
    suspend fun uploadPropertyPhotos(ownerId: String, photos: List<PickedPhoto>): Result<List<String>>
    suspend fun addProperty(
        draft: AddPropertyDraft,
        ownerId: String,
        ownerName: String,
        ownerPhone: String,
    ): Property
    suspend fun updateProperty(propertyId: String, draft: AddPropertyDraft): Property?
}
