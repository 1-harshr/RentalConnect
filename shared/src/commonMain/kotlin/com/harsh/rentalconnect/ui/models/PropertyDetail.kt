package com.harsh.rentalconnect.ui.models

data class PropertyDetail(
    val id: String,
    val name: String,
    val address: String,
    val type: String,
    val houseNumber: String,
    val isOccupied: Boolean,
    val photoUrls: List<String>,
    val tenants: List<TenantSummary>,
)
