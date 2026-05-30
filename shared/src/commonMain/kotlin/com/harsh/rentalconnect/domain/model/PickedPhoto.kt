package com.harsh.rentalconnect.domain.model

data class PickedPhoto(
    val fileName: String,
    val mimeType: String,
    val bytes: ByteArray,
)
