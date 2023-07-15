package com.example.domain.model.user

interface ClientModel {
    val id: Int
    val image: String?
    val name: String
    val surname: String
    val country: String
    val city: String
    val birthday: String?
}