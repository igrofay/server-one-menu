package com.example.feature.auth.model

import com.example.domain.model.auth.ClientSignUpModel
import com.example.domain.model.auth.RestaurantSignUpModel
import kotlinx.serialization.Serializable

@Serializable
data class ClientSignUpBody(
    override val name: String,
    override val surname: String,
    override val email: String,
    override val phoneNumber: String,
    override val password: String,
    override val country: String,
    override val city: String
) : ClientSignUpModel