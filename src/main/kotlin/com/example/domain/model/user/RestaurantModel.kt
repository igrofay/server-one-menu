package com.example.domain.model.user

interface RestaurantModel {
    val id: Int
    val image: String?
    val name: String
    val address: String
    val description: String
    val email: String
    val phone: String
}