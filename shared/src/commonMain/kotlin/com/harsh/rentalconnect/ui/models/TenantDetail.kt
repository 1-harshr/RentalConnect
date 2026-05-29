package com.harsh.rentalconnect.ui.models

data class TenantDetail(
    val id: String,
    val name: String,
    val initials: String,
    val propertyName: String,
    val flatNumber: String,
    val phone: String,
    val hometown: String,
    val aadharId: String,
    val tenantSince: String,
    val assignedPropertyName: String,
    val assignedPropertyAddress: String,
)
