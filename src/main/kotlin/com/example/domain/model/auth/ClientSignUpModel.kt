package com.example.domain.model.auth

interface ClientSignUpModel {
    val name: String
    val surname: String
    val email: String
    val phoneNumber : String
    val password: String // SHA256
    val country: String
    val city: String
}