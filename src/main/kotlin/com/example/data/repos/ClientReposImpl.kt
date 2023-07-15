package com.example.data.repos

import com.example.data.data_source.ClientService
import com.example.domain.model.user.ClientModel
import com.example.domain.model.user.UpdateClientModel
import com.example.domain.repos.ClientRepos
import io.ktor.server.application.*
import java.io.File

class ClientReposImpl(
    private val clientService: ClientService,
    private val environment: ApplicationEnvironment,
    private val imagesPath: String,
) : ClientRepos{
    override suspend fun uploadImage(clientId: Int, byteArray: ByteArray) {
        clientService.getImageName(clientId)?.let { lastNameImage->
            File("$imagesPath${File.separatorChar}$lastNameImage.jpg").delete()
        }
        val newName = System.currentTimeMillis().toString()
        File("$imagesPath${File.separatorChar}$newName.jpg").writeBytes(byteArray)
        clientService.updateImage(clientId, newName)
    }

    override suspend fun getProfile(id: Int): ClientModel {
        val clientEntity = clientService.read(id)
        val issuer = environment.config.property("jwt.issuer").getString()
        return clientEntity.copy(
            image = clientEntity.image?.let {
                "$issuer/images/$it.jpg"
            }
        )
    }

    override suspend fun updateProfile(id: Int, updateClientModel: UpdateClientModel) {
        clientService.update(id, updateClientModel)
    }

}