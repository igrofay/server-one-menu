package com.example.feature.support.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateMessageDataBody(
    val text: String,
    val date: String
)
