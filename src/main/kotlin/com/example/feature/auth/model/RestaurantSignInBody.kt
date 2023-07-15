package com.example.feature.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class RestaurantSignInBody(
    val emailOrPhone: String,
    val password: String,
)