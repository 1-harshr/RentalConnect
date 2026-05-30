package com.harsh.rentalconnect.data.repository

import com.harsh.rentalconnect.data.AppStore
import com.harsh.rentalconnect.data.normalizePhone
import com.harsh.rentalconnect.data.remote.SupabaseDataGateway
import com.harsh.rentalconnect.domain.model.Tenant
import com.harsh.rentalconnect.domain.repository.TenantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TenantRepositoryImpl(
    private val supabaseDataGateway: SupabaseDataGateway,
    private val useSupabase: Boolean,
    private val sessionTokenProvider: () -> String?,
) : TenantRepository {
    override fun getTenantById(id: String): Flow<Tenant?> =
        AppStore.tenants.map { entries -> entries.find { it.id == id } }

    override fun getTenantsForPhone(phone: String): Flow<List<Tenant>> =
        AppStore.tenants.map { entries ->
            entries.filter { normalizePhone(it.phone) == normalizePhone(phone) }
        }

    override suspend fun refreshTenantsForPhone(phone: String) {
        if (!useSupabase) return
        val remote = runCatching {
            supabaseDataGateway.fetchTenantsByPhone(
                sessionToken = sessionTokenProvider(),
                phone = phone,
            )
        }.getOrNull() ?: return
        AppStore.tenants.value = mergeByIds(AppStore.tenants.value, remote)
    }

    override suspend fun refreshTenant(tenantId: String) {
        if (!useSupabase) return
        val remote = runCatching {
            supabaseDataGateway.fetchTenantById(
                sessionToken = sessionTokenProvider(),
                tenantId = tenantId,
            )
        }.getOrNull() ?: return
        AppStore.tenants.value = mergeByIds(AppStore.tenants.value, listOf(remote))
    }

    override suspend fun addTenantToProperty(
        propertyId: String,
        tenantName: String,
        tenantPhone: String,
        hometown: String,
        aadharId: String,
    ): Tenant {
        val property = AppStore.properties.value.first { it.id == propertyId }
        val localTenant = Tenant(
            id = "t${AppStore.tenants.value.size + 1}",
            name = tenantName,
            phone = tenantPhone,
            flatNumber = "Unit ${property.tenantCount + 1}",
            since = "Today",
            propertyId = property.id,
            propertyName = property.name,
            propertyAddress = property.address,
            hometown = hometown,
            aadharId = aadharId,
        )
        val remoteTenant = if (useSupabase) {
            val tenantAccountId = runCatching {
                supabaseDataGateway.fetchTenantUserByPhone(
                    sessionToken = sessionTokenProvider(),
                    phone = tenantPhone,
                )
            }.getOrNull()?.id ?: AppStore.accounts.value.firstOrNull {
                normalizePhone(it.user.phone) == normalizePhone(tenantPhone)
            }?.user?.id
            if (tenantAccountId != null) {
                runCatching {
                    supabaseDataGateway.insertTenancy(
                        sessionToken = sessionTokenProvider(),
                        propertyId = propertyId,
                        tenantUserId = tenantAccountId,
                        flatNumber = localTenant.flatNumber,
                        since = localTenant.since,
                    )
                }.getOrNull()
            } else {
                null
            }
        } else {
            null
        }
        val tenant = remoteTenant ?: localTenant
        AppStore.tenants.value = AppStore.tenants.value + tenant
        AppStore.properties.value = AppStore.properties.value.map { current ->
            if (current.id == propertyId) current.copy(
                tenantCount = current.tenantCount + 1,
                isOccupied = true,
            ) else current
        }
        return tenant
    }

    override suspend fun removeTenant(tenantId: String) {
        val tenant = AppStore.tenants.value.find { it.id == tenantId } ?: return
        if (useSupabase) {
            runCatching {
                supabaseDataGateway.deleteTenancy(
                    sessionToken = sessionTokenProvider(),
                    tenancyId = tenantId,
                )
            }
        }
        AppStore.tenants.value = AppStore.tenants.value.filterNot { it.id == tenantId }
        val remainingCount = AppStore.tenants.value.count { it.propertyId == tenant.propertyId }
        AppStore.properties.value = AppStore.properties.value.map { property ->
            if (property.id == tenant.propertyId) {
                property.copy(
                    tenantCount = remainingCount,
                    isOccupied = remainingCount > 0,
                )
            } else {
                property
            }
        }
    }

    private fun mergeByIds(
        existing: List<Tenant>,
        incoming: List<Tenant>,
    ): List<Tenant> {
        val incomingIds = incoming.map { it.id }.toSet()
        return existing.filterNot { it.id in incomingIds } + incoming
    }
}
