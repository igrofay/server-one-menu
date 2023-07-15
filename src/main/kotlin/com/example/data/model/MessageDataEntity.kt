package com.example.data.model

import com.example.domain.model.support.MessageDataModel

data class MessageDataEntity(
    override val text: String,
    override val date: String,
    override val isSupportMessage: Boolean
) : MessageDataModel
