package com.example.data.model

import com.example.domain.model.menu.InformationRestaurantModel

data class InformationRestaurantEntity(
    override val name: String, override val description: String, override val image: String?

): InformationRestaurantModel