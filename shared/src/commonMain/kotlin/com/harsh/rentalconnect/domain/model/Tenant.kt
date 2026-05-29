package com.harsh.rentalconnect.domain.model

data class Tenant(
    val id: String,
    val name: String,
    val phone: String,
    val flatNumber: String,
    val since: String,
    val propertyId: String,
    val propertyName: String,
    val propertyAddress: String,
    val hometown: String,
    val aadharId: String,
)
