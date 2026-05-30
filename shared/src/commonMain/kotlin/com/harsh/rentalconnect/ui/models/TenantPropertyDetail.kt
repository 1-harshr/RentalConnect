package com.harsh.rentalconnect.ui.models

data class TenantPropertyDetail(
    val propertyName: String,
    val address: String,
    val flatNumber: String,
    val type: String,
    val houseNumber: String,
    val photoUrls: List<String>,
)
