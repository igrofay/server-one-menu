package com.example.data.repos

import com.example.data.data_source.SupportService
import com.example.domain.model.support.MessageDataModel
import com.example.domain.model.user.UserRole
import com.example.domain.repos.SupportRepos

class SupportReposImpl(
    private val supportService: SupportService,
) : SupportRepos {
    override suspend fun getCorrespondence(userId: Int, role: UserRole): List<MessageDataModel> {
        return supportService.readMessages(userId,role.name).reversed()
    }

    override suspend fun addMessage(userId: Int, role: UserRole, text: String, date: String): List<MessageDataModel> {
        supportService.addUserMessage(userId,role.name, text, date)
        return supportService.readMessages(userId,role.name).reversed()
    }

}