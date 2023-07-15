package com.example.domain.repos

import com.example.domain.model.menu.InformationRestaurantModel
import com.example.domain.model.user.RestaurantModel

interface RestaurantRepos {
    suspend fun getProfile(id: Int) : RestaurantModel
    suspend fun uploadImage(id: Int, image: ByteArray)
    suspend fun updateProfile(id: Int, name: String, address: String, description: String)
    suspend fun getInformationRestaurant(restaurantId :Int) : InformationRestaurantModel?
}