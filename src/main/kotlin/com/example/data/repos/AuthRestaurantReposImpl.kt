package com.example.data.repos

import com.example.data.data_source.RestaurantService
import com.example.domain.model.auth.RestaurantSignUpModel
import com.example.domain.repos.AuthRestaurantRepos

class AuthRestaurantReposImpl(
    private val restaurantService: RestaurantService,
): AuthRestaurantRepos {
    override suspend fun checkIfPhoneNumberIsRegistered(phone: String): Boolean {
        return restaurantService.phoneIsRegistered(phone)
    }

    override suspend fun checkIfEmailIsRegistered(email: String): Boolean {
        return restaurantService.emailIsRegistered(email)
    }

    override suspend fun createRestaurant(restaurantSignUpModel: RestaurantSignUpModel): Int {
        return restaurantService.create(restaurantSignUpModel)
    }

    override suspend fun getIdFromEmail(email: String, password: String): Int? {
        return restaurantService.getIdFromEmail(email, password)
    }

    override suspend fun getIdFromPhone(phone: String, password: String): Int? {
        return restaurantService.getIdFromPhone(phone, password)
    }

}