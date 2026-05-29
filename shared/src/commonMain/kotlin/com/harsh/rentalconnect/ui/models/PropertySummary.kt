package com.harsh.rentalconnect.ui.models

data class PropertySummary(
    val id: String,
    val name: String,
    val address: String,
    val tenantCount: Int,
    val type: String,
    val isOccupied: Boolean,
)
