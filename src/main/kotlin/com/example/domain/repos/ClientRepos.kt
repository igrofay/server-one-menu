package com.example.domain.repos

import com.example.domain.model.user.ClientModel
import com.example.domain.model.user.UpdateClientModel

interface ClientRepos {
    suspend fun uploadImage(clientId: Int, byteArray: ByteArray)
    suspend fun getProfile(id: Int) : ClientModel
    suspend fun updateProfile(id: Int, updateClientModel: UpdateClientModel)
}