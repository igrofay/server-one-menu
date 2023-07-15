package com.example.data.data_source

import com.example.data.model.MessageDataEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.h2.jdbc.JdbcSQLSyntaxErrorException
import java.sql.Connection
import java.sql.Date
import java.sql.Statement
import java.time.LocalDate

class SupportService (
    private val connection: Connection,
){
    companion object{
        private const val CREATE_TABLE_MESSAGES =
            "CREATE TABLE MESSAGES (idUser INTEGER, role VARCHAR(50), text VARCHAR(150), dateOfCreation VARCHAR(50), isSupportMessage BOOLEAN);"
        private const val SELECT_USER_MESSAGES =
            "SELECT text, dateOfCreation, isSupportMessage FROM MESSAGES WHERE idUser = ? AND role = ?"
        private const val INSERT_USER_MESSAGE =
            "INSERT INTO MESSAGES (idUser, role, text, dateOfCreation, isSupportMessage) VALUES (?, ?, ?, ?, ?)"
    }
    init {
        val statement = connection.createStatement()
        try {
            statement.executeUpdate(CREATE_TABLE_MESSAGES)
        } catch (e: JdbcSQLSyntaxErrorException) {
            println(e)
        }
    }
    suspend fun readMessages(userId:Int, role: String) = withContext(Dispatchers.IO){
        val statement = connection.prepareStatement(SELECT_USER_MESSAGES)
        statement.setInt(1, userId)
        statement.setString(2, role)
        val answer = mutableListOf<MessageDataEntity>()
        val resultSet = statement.executeQuery()
        while (resultSet.next()){
            answer.add(
                MessageDataEntity(
                    text = resultSet.getString("text"),
                    isSupportMessage = resultSet.getBoolean("isSupportMessage"),
                    date = resultSet.getString("dateOfCreation")
                )
            )
        }
        return@withContext answer
    }
    suspend fun addUserMessage(userId: Int, role:String, text:String, dataOfCreation: String) = withContext(Dispatchers.IO){
        val statement = connection.prepareStatement(INSERT_USER_MESSAGE, Statement.NO_GENERATED_KEYS)
        statement.setInt(1, userId)
        statement.setString(2, role, )
        statement.setString(3, text)
        statement.setString(4, dataOfCreation)
        statement.setBoolean(5, false)
        statement.executeUpdate()
    }
}