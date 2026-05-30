package com.harsh.rentalconnect.navigation

import kotlinx.serialization.Serializable

sealed interface AppRoute {

    // ── Auth ─────────────────────────────────────────────────────────────────
    @Serializable
    data object Onboarding : AppRoute

    @Serializable
    data object SignIn : AppRoute

    @Serializable
    data object Account : AppRoute

    // ── Owner flow ────────────────────────────────────────────────────────────
    @Serializable
    data object OwnerHome : AppRoute

    @Serializable
    data class PropertyDetailOwner(val propertyId: String) : AppRoute

    @Serializable
    data class AddTenant(val propertyId: String) : AppRoute

    @Serializable
    data class TenantProfile(val propertyId: String, val tenantId: String) : AppRoute

    @Serializable
    data object AddProperty : AppRoute

    @Serializable
    data class EditProperty(val propertyId: String) : AppRoute

    // ── Tenant flow ───────────────────────────────────────────────────────────
    @Serializable
    data object TenantHome : AppRoute

    @Serializable
    data class PropertyDetailTenant(val propertyId: String) : AppRoute
}
