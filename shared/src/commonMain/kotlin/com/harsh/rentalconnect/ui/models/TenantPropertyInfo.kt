package com.harsh.rentalconnect.ui.models

data class TenantPropertyInfo(
    val propertyId: String,
    val propertyName: String,
    val address: String,
    val houseNumber: String,
    val flatNumber: String,
    val type: String,
    val isOccupied: Boolean,
    val ownerName: String,
    val ownerPhone: String,
)
