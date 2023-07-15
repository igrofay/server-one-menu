package com.example.data.model

import com.example.domain.model.user.RestaurantModel

data class RestaurantEntity(
    override val name: String,
    override val address: String,
    override val description: String,
    override val email: String,
    override val phone: String,
    override val id: Int,
    override val image: String?,
): RestaurantModel
