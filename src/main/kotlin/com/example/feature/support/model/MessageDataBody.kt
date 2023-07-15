package com.example.feature.support.model

import com.example.domain.model.support.MessageDataModel
import kotlinx.serialization.Serializable

@Serializable
data class MessageDataBody(
    override val text: String,
    override val date: String,
    override val isSupportMessage: Boolean
) : MessageDataModel{
    companion object{
        private fun MessageDataModel.fromModelToMessageDataBody() = MessageDataBody(text, date, isSupportMessage)
        fun List<MessageDataModel>.fromListModelToListMessageData() = this.map { it.fromModelToMessageDataBody() }
    }
}
