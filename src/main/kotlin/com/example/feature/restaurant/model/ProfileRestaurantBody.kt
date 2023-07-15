package com.example.feature.restaurant.model

import com.example.domain.model.user.RestaurantModel
import kotlinx.serialization.Serializable

@Serializable
data class ProfileRestaurantBody(
    override val id: Int,
    override val image: String?,
    override val name: String,
    override val address: String,
    override val description: String,
    override val email: String,
    override val phone: String
) : RestaurantModel{
    companion object{
        fun RestaurantModel.fromModelToProfileRestaurantBody() = ProfileRestaurantBody(
            id, image, name, address, description, email, phone
        )
    }
}