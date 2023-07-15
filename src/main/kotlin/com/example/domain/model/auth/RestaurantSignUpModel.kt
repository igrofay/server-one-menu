package com.example.domain.model.auth

interface RestaurantSignUpModel {
    val name: String
    val address: String
    val description: String
    val email: String
    val phoneNumber: String
    val password: String // SHA256
}