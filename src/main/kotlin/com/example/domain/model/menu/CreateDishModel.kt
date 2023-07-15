package com.example.domain.model.menu

interface CreateDishModel {
    val name: String
    val description: String
    val components: String
    val categoryId : Int
    val spicinessLevel: Int
    val price: Float
    val currencyCode: String
}