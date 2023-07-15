package com.example.feature.restaurant.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateCategoryBody(
    val category: String,
)
