package com.example.feature.client.model

import com.example.domain.model.user.ClientModel
import kotlinx.serialization.Serializable

@Serializable
data class ClientBody(
    override val id: Int,
    override val image: String?,
    override val name: String,
    override val surname: String,
    override val country: String,
    override val city: String,
    override val birthday: String?
) : ClientModel{
    companion object{
        fun ClientModel.fromModelToClientBody() = ClientBody(
            id, image, name, surname, country, city, birthday
        )
    }
}