package com.example.feature.restaurant.model

import com.example.domain.model.menu.CreateDishModel
import kotlinx.serialization.Serializable

@Serializable
data class CreateDishBody(
    override val name: String,
    override val description: String,
    override val components: String,
    override val categoryId: Int,
    override val spicinessLevel: Int,
    override val price: Float,
    override val currencyCode: String
) : CreateDishModel