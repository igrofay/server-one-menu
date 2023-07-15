package com.example.feature.restaurant.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateRestaurantBody(
    val name: String,
    val address: String,
    val description:String,
)
