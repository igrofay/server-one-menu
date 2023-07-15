package com.example.data.model

import com.example.domain.model.menu.DishModel

data class DishEntity(
    override val id: Int,
    override val image: String?,
    override val name: String,
    override val description: String,
    override val components: String,
    override val categoriesId: Int,
    override val spicinessLevel: Int,
    override val price: Float,
    override val currency: String
) : DishModel