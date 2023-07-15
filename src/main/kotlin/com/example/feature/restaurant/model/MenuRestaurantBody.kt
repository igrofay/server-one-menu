package com.example.feature.restaurant.model

import kotlinx.serialization.Serializable

@Serializable
data class MenuRestaurantBody(
    val categories: List<CategoryBody>,
    val dishes: List<DishBody>
)
