package com.harsh.rentalconnect.domain.model

data class Property(
    val id: String,
    val ownerId: String,
    val name: String,
    val address: String,
    val type: String,
    val houseNumber: String,
    val isOccupied: Boolean,
    val tenantCount: Int,
    val ownerName: String = "",
    val ownerPhone: String = "",
    val photoUrls: List<String> = emptyList(),
)
