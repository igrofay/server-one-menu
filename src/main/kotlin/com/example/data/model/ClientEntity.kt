package com.example.data.model

import com.example.domain.model.user.ClientModel

data class ClientEntity(
    override val id: Int,
    override val image: String?,
    override val name: String,
    override val surname: String,
    override val country: String,
    override val city: String,
    override val birthday: String?,
) : ClientModel
