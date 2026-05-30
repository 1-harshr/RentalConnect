package com.harsh.rentalconnect.data.remote

import com.harsh.rentalconnect.domain.model.AddPropertyDraft
import com.harsh.rentalconnect.domain.model.AuthUser
import com.harsh.rentalconnect.domain.model.PickedPhoto
import com.harsh.rentalconnect.domain.model.ProfileUpdateRequest
import com.harsh.rentalconnect.domain.model.Property
import com.harsh.rentalconnect.domain.model.Tenant
import com.harsh.rentalconnect.ui.models.Role
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Clock

class SupabaseDataGateway(
    private val httpClient: HttpClient,
    private val config: SupabaseConfig,
) {
    private val propertyPhotosBucket = "property-photos"

    suspend fun fetchProperties(
        sessionToken: String?,
        ownerId: String? = null,
    ): List<Property> {
        require(config.isConfigured)
        val ownerFilter = ownerId?.let { "&owner_id=eq.$it" }.orEmpty()
        val rows: List<PropertyRow> = httpClient.get("${config.url}/rest/v1/properties_with_counts?select=*&order=name.asc$ownerFilter") {
            applyHeaders(sessionToken)
        }.body()
        return rows.map { it.toDomain() }
    }

    suspend fun fetchPropertyById(
        sessionToken: String?,
        propertyId: String,
    ): Property? {
        require(config.isConfigured)
        val rows: List<PropertyRow> = httpClient.get("${config.url}/rest/v1/properties_with_counts?select=*&id=eq.$propertyId&limit=1") {
            applyHeaders(sessionToken)
        }.body()
        return rows.firstOrNull()?.toDomain()
    }

    suspend fun fetchUserProfile(
        sessionToken: String?,
        userId: String,
    ): AuthUser? {
        require(config.isConfigured)
        val rows: List<UserProfileRow> = httpClient.get("${config.url}/rest/v1/users?select=*&id=eq.$userId&limit=1") {
            applyHeaders(sessionToken)
        }.body()
        return rows.firstOrNull()?.toDomain()
    }

    suspend fun upsertUserProfile(
        sessionToken: String?,
        user: AuthUser,
    ): AuthUser {
        require(config.isConfigured)
        val rows: List<UserProfileRow> = httpClient.post("${config.url}/rest/v1/users") {
            applyHeaders(sessionToken)
            header("Prefer", "resolution=merge-duplicates,return=representation")
            contentType(ContentType.Application.Json)
            setBody(listOf(UserProfilePayload.from(user)))
        }.body()
        return rows.first().toDomain()
    }

    suspend fun updateUserProfile(
        sessionToken: String?,
        userId: String,
        request: ProfileUpdateRequest,
    ): AuthUser? {
        require(config.isConfigured)
        val rows: List<UserProfileRow> = httpClient.patch("${config.url}/rest/v1/users?id=eq.$userId") {
            applyHeaders(sessionToken)
            header("Prefer", "return=representation")
            contentType(ContentType.Application.Json)
            setBody(
                UserProfileUpdatePayload(
                    name = request.name.trim(),
                    phone = request.phone.trim(),
                    hometown = request.hometown.trim(),
                    aadharId = request.aadharId.trim(),
                )
            )
        }.body()
        return rows.firstOrNull()?.toDomain()
    }

    suspend fun fetchTenantUserByPhone(
        sessionToken: String?,
        phone: String,
    ): AuthUser? {
        require(config.isConfigured)
        val normalized = phone.trim()
        val rows: List<UserProfileRow> = httpClient.get(
            "${config.url}/rest/v1/users?select=*&role=eq.tenant&phone=eq.${encode(normalized)}&limit=1"
        ) {
            applyHeaders(sessionToken)
        }.body()
        return rows.firstOrNull()?.toDomain()
    }

    suspend fun fetchTenantsForProperty(
        sessionToken: String?,
        propertyId: String,
    ): List<Tenant> {
        require(config.isConfigured)
        val rows: List<TenantJoinRow> = httpClient.get(
            "${config.url}/rest/v1/tenancies?select=id,property_id,flat_number,since,users!inner(id,name,phone,hometown,aadhar_id),properties!inner(id,name,address)&property_id=eq.$propertyId"
        ) {
            applyHeaders(sessionToken)
        }.body()
        return rows.mapNotNull { it.toTenant() }
    }

    suspend fun fetchTenantsByPhone(
        sessionToken: String?,
        phone: String,
    ): List<Tenant> {
        require(config.isConfigured)
        val rows: List<TenantJoinRow> = httpClient.get(
            "${config.url}/rest/v1/tenancies?select=id,property_id,flat_number,since,users!inner(id,name,phone,hometown,aadhar_id),properties!inner(id,name,address)&users.phone=eq.${encode(phone)}"
        ) {
            applyHeaders(sessionToken)
        }.body()
        return rows.mapNotNull { it.toTenant() }
    }

    suspend fun fetchTenantById(
        sessionToken: String?,
        tenantId: String,
    ): Tenant? {
        require(config.isConfigured)
        val rows: List<TenantJoinRow> = httpClient.get(
            "${config.url}/rest/v1/tenancies?select=id,property_id,flat_number,since,users!inner(id,name,phone,hometown,aadhar_id),properties!inner(id,name,address)&id=eq.$tenantId&limit=1"
        ) {
            applyHeaders(sessionToken)
        }.body()
        return rows.firstOrNull()?.toTenant()
    }

    suspend fun insertProperty(
        sessionToken: String?,
        owner: AuthUser,
        draft: AddPropertyDraft,
    ): Property {
        require(config.isConfigured)
        val rows: List<PropertyRow> = httpClient.post("${config.url}/rest/v1/properties") {
            applyHeaders(sessionToken)
            header("Prefer", "return=representation")
            contentType(ContentType.Application.Json)
            setBody(
                listOf(
                    PropertyPayload(
                        ownerId = owner.id,
                        name = draft.propertyName.trim(),
                        address = draft.address.trim(),
                        hno = draft.houseNumber.trim(),
                        status = if (draft.isOccupied) "occupied" else "available",
                        type = draft.type.trim(),
                        photos = draft.photoUrls,
                        ownerName = owner.name,
                        ownerPhone = owner.phone,
                    )
                )
            )
        }.body()
        return rows.first().toDomain()
    }

    suspend fun updateProperty(
        sessionToken: String?,
        propertyId: String,
        draft: AddPropertyDraft,
    ): Property? {
        require(config.isConfigured)
        val rows: List<PropertyRow> = httpClient.patch("${config.url}/rest/v1/properties?id=eq.$propertyId") {
            applyHeaders(sessionToken)
            header("Prefer", "return=representation")
            contentType(ContentType.Application.Json)
            setBody(
                PropertyUpdatePayload(
                    name = draft.propertyName.trim(),
                    address = draft.address.trim(),
                    hno = draft.houseNumber.trim(),
                    status = if (draft.isOccupied) "occupied" else "available",
                    type = draft.type.trim(),
                    photos = draft.photoUrls,
                )
            )
        }.body()
        return rows.firstOrNull()?.toDomain()
    }

    suspend fun insertTenancy(
        sessionToken: String?,
        propertyId: String,
        tenantUserId: String,
        flatNumber: String,
        since: String,
    ): Tenant {
        require(config.isConfigured)
        val tenancyRows: List<TenancyInsertRow> = httpClient.post("${config.url}/rest/v1/tenancies") {
            applyHeaders(sessionToken)
            header("Prefer", "return=representation")
            contentType(ContentType.Application.Json)
            setBody(
                listOf(
                    TenancyPayload(
                        propertyId = propertyId,
                        tenantId = tenantUserId,
                        flatNumber = flatNumber,
                        since = since,
                    )
                )
            )
        }.body()
        val inserted = tenancyRows.first()
        return fetchTenantsForProperty(sessionToken, inserted.propertyId).first { it.id == inserted.id }
    }

    suspend fun uploadPropertyPhotos(
        sessionToken: String?,
        ownerId: String,
        photos: List<PickedPhoto>,
    ): List<String> {
        require(config.isConfigured)
        return photos.mapIndexed { index, photo ->
            val objectPath = buildPropertyPhotoPath(
                ownerId = ownerId,
                photo = photo,
                index = index,
            )
            httpClient.put("${config.url}/storage/v1/object/$propertyPhotosBucket/$objectPath") {
                applyHeaders(sessionToken)
                header("x-upsert", "true")
                contentType(ContentType.parse(photo.mimeType))
                setBody(photo.bytes)
            }
            "${config.url}/storage/v1/object/public/$propertyPhotosBucket/$objectPath"
        }
    }

    suspend fun deleteTenancy(
        sessionToken: String?,
        tenancyId: String,
    ) {
        require(config.isConfigured)
        httpClient.delete("${config.url}/rest/v1/tenancies?id=eq.$tenancyId") {
            applyHeaders(sessionToken)
        }
    }

    private fun PropertyRow.toDomain(): Property = Property(
        id = id,
        ownerId = ownerId,
        name = name,
        address = address,
        type = type,
        houseNumber = hno,
        isOccupied = status.equals("occupied", ignoreCase = true),
        tenantCount = tenantCount,
        ownerName = ownerName,
        ownerPhone = ownerPhone,
        photoUrls = photos,
    )

    private fun UserProfileRow.toDomain(): AuthUser = AuthUser(
        id = id,
        name = name,
        email = email,
        phone = phone,
        role = if (role.equals("tenant", ignoreCase = true)) Role.Tenant else Role.Owner,
        hometown = hometown,
        aadharId = aadharId,
    )

    private fun TenantJoinRow.toTenant(): Tenant? {
        val user = user ?: return null
        val property = property ?: return null
        return Tenant(
            id = id,
            name = user.name,
            phone = user.phone,
            flatNumber = flatNumber,
            since = since,
            propertyId = propertyId,
            propertyName = property.name,
            propertyAddress = property.address,
            hometown = user.hometown,
            aadharId = user.aadharId,
        )
    }

    private fun HttpRequestBuilder.applyHeaders(sessionToken: String?) {
        header("apikey", config.anonKey)
        header(
            HttpHeaders.Authorization,
            "Bearer ${sessionToken?.takeIf { it.isNotBlank() } ?: config.anonKey}",
        )
    }

    private fun encode(value: String): String = value.replace(" ", "%20").replace("+", "%2B")

    private fun buildPropertyPhotoPath(
        ownerId: String,
        photo: PickedPhoto,
        index: Int,
    ): String {
        val sanitizedName = photo.fileName
            .substringBeforeLast('.')
            .lowercase()
            .replace(Regex("[^a-z0-9-_]+"), "-")
            .trim('-')
            .ifBlank { "photo-${index + 1}" }
        val extension = photo.fileName.substringAfterLast('.', missingDelimiterValue = photo.mimeType.toExtension())
        val timestamp = Clock.System.now().toEpochMilliseconds()
        return "$ownerId/${timestamp}-${index + 1}-$sanitizedName.$extension"
    }

    private fun String.toExtension(): String = when (lowercase()) {
        "image/png" -> "png"
        "image/webp" -> "webp"
        "image/heic" -> "heic"
        else -> "jpg"
    }
}

@Serializable
private data class PropertyRow(
    val id: String,
    @SerialName("owner_id") val ownerId: String,
    val name: String,
    val address: String,
    val type: String = "",
    val status: String = "available",
    val photos: List<String> = emptyList(),
    @SerialName("hno") val hno: String,
    @SerialName("tenant_count") val tenantCount: Int = 0,
    @SerialName("owner_name") val ownerName: String = "",
    @SerialName("owner_phone") val ownerPhone: String = "",
)

@Serializable
private data class PropertyPayload(
    @SerialName("owner_id") val ownerId: String,
    val name: String,
    val address: String,
    val type: String,
    val status: String,
    val photos: List<String>,
    @SerialName("hno") val hno: String,
    @SerialName("owner_name") val ownerName: String,
    @SerialName("owner_phone") val ownerPhone: String,
)

@Serializable
private data class PropertyUpdatePayload(
    val name: String,
    val address: String,
    val type: String,
    val status: String,
    val photos: List<String>,
    @SerialName("hno") val hno: String,
)

@Serializable
private data class UserProfileRow(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val hometown: String = "",
    @SerialName("aadhar_id") val aadharId: String = "",
    val role: String,
)

@Serializable
private data class UserProfilePayload(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val hometown: String,
    @SerialName("aadhar_id") val aadharId: String,
    val role: String,
) {
    companion object {
        fun from(user: AuthUser): UserProfilePayload = UserProfilePayload(
            id = user.id,
            name = user.name.trim(),
            email = user.email.trim(),
            phone = user.phone.trim(),
            hometown = user.hometown.trim(),
            aadharId = user.aadharId.trim(),
            role = user.role.name.lowercase(),
        )
    }
}

@Serializable
private data class UserProfileUpdatePayload(
    val name: String,
    val phone: String,
    val hometown: String,
    @SerialName("aadhar_id") val aadharId: String,
)

@Serializable
private data class TenantJoinRow(
    val id: String,
    @SerialName("property_id") val propertyId: String,
    @SerialName("flat_number") val flatNumber: String = "",
    val since: String = "",
    @SerialName("users") val user: UserRow? = null,
    @SerialName("properties") val property: PropertyStubRow? = null,
)

@Serializable
private data class UserRow(
    val id: String,
    val name: String,
    val phone: String,
    val hometown: String = "",
    @SerialName("aadhar_id") val aadharId: String = "",
)

@Serializable
private data class PropertyStubRow(
    val id: String,
    val name: String,
    val address: String,
)

@Serializable
private data class TenancyPayload(
    @SerialName("property_id") val propertyId: String,
    @SerialName("tenant_id") val tenantId: String,
    @SerialName("flat_number") val flatNumber: String,
    val since: String,
)

@Serializable
private data class TenancyInsertRow(
    val id: String,
    @SerialName("property_id") val propertyId: String,
)
