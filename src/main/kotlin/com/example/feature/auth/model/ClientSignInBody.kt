package com.example.feature.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class ClientSignInBody(
    val emailOrPhone: String,
    val password: String,
)
