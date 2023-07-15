package com.example.domain.repos

import com.example.domain.model.support.MessageDataModel
import com.example.domain.model.user.UserRole

interface SupportRepos {
    suspend fun getCorrespondence(userId: Int, role: UserRole) : List<MessageDataModel>
    suspend fun addMessage(userId: Int, role: UserRole, text: String, date: String) : List<MessageDataModel>
}