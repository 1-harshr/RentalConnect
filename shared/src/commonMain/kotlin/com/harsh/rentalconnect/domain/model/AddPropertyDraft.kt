package com.harsh.rentalconnect.domain.model

data class AddPropertyDraft(
    val propertyName: String,
    val houseNumber: String,
    val address: String,
    val type: String,
    val isOccupied: Boolean,
    val photoUrls: List<String>,
)
