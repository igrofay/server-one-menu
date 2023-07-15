package com.example.feature.auth.model

import com.example.domain.model.auth.RestaurantSignUpModel
import kotlinx.serialization.Serializable

@Serializable
data class RestaurantSignUpBody(
    override val name: String,
    override val address: String,
    override val description: String,
    override val email: String,
    override val phoneNumber: String,
    override val password: String
) : RestaurantSignUpModel