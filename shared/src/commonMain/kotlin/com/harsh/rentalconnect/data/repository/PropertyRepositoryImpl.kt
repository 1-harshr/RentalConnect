package com.harsh.rentalconnect.data.repository

import com.harsh.rentalconnect.data.AppStore
import com.harsh.rentalconnect.data.remote.SupabaseDataGateway
import com.harsh.rentalconnect.domain.model.AddPropertyDraft
import com.harsh.rentalconnect.domain.model.PickedPhoto
import com.harsh.rentalconnect.domain.model.Property
import com.harsh.rentalconnect.domain.model.Tenant
import com.harsh.rentalconnect.domain.repository.PropertyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PropertyRepositoryImpl(
    private val supabaseDataGateway: SupabaseDataGateway,
    private val useSupabase: Boolean,
    private val sessionTokenProvider: () -> String?,
) : PropertyRepository {
    override fun getProperties(ownerId: String?): Flow<List<Property>> =
        AppStore.properties.map { entries ->
            if (ownerId == null) entries else entries.filter { it.ownerId == ownerId }
        }

    override fun getPropertyById(id: String): Flow<Property?> =
        AppStore.properties.map { entries -> entries.find { it.id == id } }

    override fun getTenantsForProperty(propertyId: String): Flow<List<Tenant>> =
        AppStore.tenants.map { entries -> entries.filter { it.propertyId == propertyId } }

    override suspend fun refreshProperties(ownerId: String?) {
        if (!useSupabase) return
        val remote = runCatching {
            supabaseDataGateway.fetchProperties(
                sessionToken = sessionTokenProvider(),
                ownerId = ownerId,
            )
        }.getOrNull() ?: return

        AppStore.properties.value = mergeProperties(AppStore.properties.value, remote)
    }

    override suspend fun refreshPropertyDetail(propertyId: String) {
        if (!useSupabase) return
        val remoteProperty = runCatching {
            supabaseDataGateway.fetchPropertyById(
                sessionToken = sessionTokenProvider(),
                propertyId = propertyId,
            )
        }.getOrNull()
        val remoteTenants = runCatching {
            supabaseDataGateway.fetchTenantsForProperty(
                sessionToken = sessionTokenProvider(),
                propertyId = propertyId,
            )
        }.getOrNull()

        if (remoteProperty != null) {
            AppStore.properties.value = mergeProperties(AppStore.properties.value, listOf(remoteProperty))
        }
        if (remoteTenants != null) {
            AppStore.tenants.value = mergeTenants(AppStore.tenants.value, propertyId, remoteTenants)
        }
    }

    override suspend fun uploadPropertyPhotos(
        ownerId: String,
        photos: List<PickedPhoto>,
    ): Result<List<String>> {
        if (photos.isEmpty()) return Result.success(emptyList())
        if (!useSupabase) {
            return Result.failure(
                IllegalStateException("Photo upload requires a configured Supabase backend."),
            )
        }

        return runCatching {
            supabaseDataGateway.uploadPropertyPhotos(
                sessionToken = sessionTokenProvider(),
                ownerId = ownerId,
                photos = photos,
            )
        }
    }

    override suspend fun addProperty(
        draft: AddPropertyDraft,
        ownerId: String,
        ownerName: String,
        ownerPhone: String,
    ): Property {
        val localProperty = Property(
            id = "p${AppStore.properties.value.size + 1}",
            ownerId = ownerId,
            name = draft.propertyName.trim(),
            address = draft.address.trim(),
            type = draft.type.trim(),
            houseNumber = draft.houseNumber.trim(),
            isOccupied = draft.isOccupied,
            tenantCount = 0,
            ownerName = ownerName,
            ownerPhone = ownerPhone,
            photoUrls = draft.photoUrls,
        )
        val remoteProperty = if (useSupabase) {
            runCatching {
                supabaseDataGateway.insertProperty(
                    sessionToken = sessionTokenProvider(),
                    owner = com.harsh.rentalconnect.domain.model.AuthUser(
                        id = ownerId,
                        name = ownerName,
                        email = "",
                        phone = ownerPhone,
                        role = com.harsh.rentalconnect.ui.models.Role.Owner,
                        hometown = "",
                        aadharId = "",
                    ),
                    draft = draft,
                )
            }.getOrNull()
        } else {
            null
        }
        val property = remoteProperty ?: localProperty
        AppStore.properties.value = AppStore.properties.value + property
        return property
    }

    override suspend fun updateProperty(propertyId: String, draft: AddPropertyDraft): Property? {
        val existing = AppStore.properties.value.find { it.id == propertyId } ?: return null
        val localUpdated = existing.copy(
            name = draft.propertyName.trim(),
            address = draft.address.trim(),
            type = draft.type.trim(),
            houseNumber = draft.houseNumber.trim(),
            isOccupied = draft.isOccupied,
            photoUrls = draft.photoUrls,
        )
        val updated = if (useSupabase) {
            runCatching {
                supabaseDataGateway.updateProperty(
                    sessionToken = sessionTokenProvider(),
                    propertyId = propertyId,
                    draft = draft,
                )
            }.getOrNull() ?: localUpdated
        } else {
            localUpdated
        }
        AppStore.properties.value = AppStore.properties.value.map { property ->
            if (property.id == propertyId) updated else property
        }
        AppStore.tenants.value = AppStore.tenants.value.map { tenant ->
            if (tenant.propertyId == propertyId) {
                tenant.copy(
                    propertyName = updated.name,
                    propertyAddress = updated.address,
                )
            } else {
                tenant
            }
        }
        return updated
    }

    private fun mergeProperties(
        existing: List<Property>,
        incoming: List<Property>,
    ): List<Property> {
        val incomingIds = incoming.map { it.id }.toSet()
        return existing.filterNot { it.id in incomingIds } + incoming
    }

    private fun mergeTenants(
        existing: List<Tenant>,
        propertyId: String,
        incoming: List<Tenant>,
    ): List<Tenant> {
        return existing.filterNot { it.propertyId == propertyId } + incoming
    }
}
