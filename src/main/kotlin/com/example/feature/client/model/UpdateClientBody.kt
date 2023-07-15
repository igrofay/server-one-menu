package com.example.feature.client.model

import com.example.domain.model.user.UpdateClientModel
import kotlinx.serialization.Serializable

@Serializable
data class UpdateClientBody(
    override val name: String,
    override val surname: String,
    override val birthday: String?,
    override val country: String,
    override val city: String
) : UpdateClientModel
