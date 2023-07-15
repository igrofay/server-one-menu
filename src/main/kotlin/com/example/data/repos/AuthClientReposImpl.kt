package com.example.data.repos

import com.example.data.data_source.ClientService
import com.example.domain.model.auth.ClientSignUpModel
import com.example.domain.repos.AuthClientRepos

class AuthClientReposImpl(
    private val clientService: ClientService,
) : AuthClientRepos {
    override suspend fun checkIfPhoneNumberIsRegistered(phone: String): Boolean {
        return clientService.phoneIsRegistered(phone)
    }

    override suspend fun checkIfEmailIsRegistered(email: String): Boolean {
        return clientService.emailIsRegistered(email)
    }

    override suspend fun createClient(clientSignUpModel: ClientSignUpModel): Int {
        return clientService.create(clientSignUpModel)
    }

    override suspend fun getIdFromEmail(email: String, password: String): Int? {
        return clientService.getIdFromEmail(email, password)
    }

    override suspend fun getIdFromPhone(phone: String, password: String): Int? {
        return clientService.getIdFromPhone(phone, password)
    }
}