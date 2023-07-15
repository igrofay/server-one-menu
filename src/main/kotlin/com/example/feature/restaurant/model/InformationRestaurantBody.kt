package com.example.feature.restaurant.model

import com.example.domain.model.menu.InformationRestaurantModel
import kotlinx.serialization.Serializable

@Serializable
data class InformationRestaurantBody(
    override val name: String, override val description: String, override val image: String?
) : InformationRestaurantModel{
    companion object{
        fun InformationRestaurantModel.fromModelToInformationRestaurantBody() = InformationRestaurantBody(
            name, description, image
        )
    }
}