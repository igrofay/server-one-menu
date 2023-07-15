package com.example.domain.repos

import com.example.domain.model.auth.ClientSignUpModel

interface AuthClientRepos {
    suspend fun checkIfPhoneNumberIsRegistered(phone: String): Boolean
    suspend fun checkIfEmailIsRegistered(email: String) : Boolean
    suspend fun createClient(clientSignUpModel: ClientSignUpModel) : Int // id
    suspend fun getIdFromEmail(email: String, password: String): Int?
    suspend fun getIdFromPhone(phone: String, password: String): Int?
}