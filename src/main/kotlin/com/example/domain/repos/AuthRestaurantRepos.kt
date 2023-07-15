package com.example.domain.repos

import com.example.domain.model.auth.RestaurantSignUpModel

interface AuthRestaurantRepos {
    suspend fun checkIfPhoneNumberIsRegistered(phone: String): Boolean
    suspend fun checkIfEmailIsRegistered(email: String) : Boolean
    suspend fun createRestaurant(restaurantSignUpModel: RestaurantSignUpModel) : Int // id
    suspend fun getIdFromEmail(email: String, password: String): Int?
    suspend fun getIdFromPhone(phone: String, password: String): Int?
}