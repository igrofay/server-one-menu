package com.example.feature.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class PhoneBody(
    val phoneNumber: String,
)
