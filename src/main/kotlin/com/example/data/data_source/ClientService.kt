package com.example.data.data_source

import com.example.data.model.ClientEntity
import com.example.data.model.RestaurantEntity
import com.example.domain.model.auth.ClientSignUpModel
import com.example.domain.model.user.UpdateClientModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.h2.jdbc.JdbcSQLSyntaxErrorException
import java.sql.Connection
import java.sql.Date
import java.sql.Statement

class ClientService(
    private val connection: Connection
) {
    companion object{
        private const val CREATE_CLIENTS_TABLE =
            "CREATE TABLE CLIENTS (ID SERIAL PRIMARY KEY, image VARCHAR(30) NULL, name VARCHAR(50), surname VARCHAR(50), email VARCHAR(80), phone VARCHAR(25), password VARCHAR(64), country VARCHAR(50), city VARCHAR(80), birthday DATE NULL);"
        private const val SEARCH_PHONE_NUMBER =
            "SELECT phone FROM CLIENTS WHERE phone = ?;"
        private const val SEARCH_EMAIL =
            "SELECT email FROM CLIENTS WHERE email = ?;"
        private const val UPDATE_CLIENT_IMAGE =
            "UPDATE CLIENTS SET image = ? WHERE id = ?"
        private const val SEARCH_CLIENT_IMAGE =
            "SELECT image FROM CLIENTS WHERE id = ?;"
        private const val SEARCH_ID_BY_EMAIL_AND_PASSWORD =
            "SELECT id FROM CLIENTS WHERE email = ? AND password = ?;"
        private const val SEARCH_ID_BY_PHONE_AND_PASSWORD =
            "SELECT id FROM CLIENTS WHERE phone = ? AND password = ?;"
        private const val INSERT_CLIENT =
            "INSERT INTO CLIENTS (name, surname, email, phone, password, country, city) VALUES (?, ?, ?, ?, ?, ?, ?)"
        private const val SELECT_CLIENT_BY_ID =
            "SELECT id, image, name, surname, country, city, birthday FROM CLIENTS WHERE id = ?"
        private const val UPDATE_CLIENT_DATA =
            "UPDATE CLIENTS SET name = ?, surname = ?, birthday = ?, country = ?, city = ? WHERE id = ?"
    }
    init {
        val statement = connection.createStatement()
        try {
            statement.executeUpdate(CREATE_CLIENTS_TABLE)
        } catch (e: JdbcSQLSyntaxErrorException) {
            println(e)
        }
    }
    suspend fun phoneIsRegistered(phone: String) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SEARCH_PHONE_NUMBER)
        statement.setString(1, phone)
        val resultSet = statement.executeQuery()
        return@withContext resultSet.next()
    }

    suspend fun emailIsRegistered(email: String) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SEARCH_EMAIL)
        statement.setString(1, email)
        val resultSet = statement.executeQuery()
        return@withContext resultSet.next()
    }

    suspend fun getImageName(id: Int) = withContext(Dispatchers.IO){
        val statement = connection.prepareStatement(SEARCH_CLIENT_IMAGE)
        statement.setInt(1, id)
        val resultSet = statement.executeQuery()
        return@withContext if (resultSet.next()) {
            resultSet.getString("image")
        } else {
            null
        }
    }
    suspend fun updateImage(id:Int, name: String) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_CLIENT_IMAGE)
        statement.setString(1, name)
        statement.setInt(2, id)
        statement.executeUpdate()
    }
    suspend fun getIdFromPhone(phone: String, password: String) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SEARCH_ID_BY_PHONE_AND_PASSWORD)
        statement.setString(1, phone)
        statement.setString(2, password)
        val resultSet = statement.executeQuery()
        return@withContext if (resultSet.next()) {
            resultSet.getInt("id")
        } else {
            null
        }

    }

    suspend fun getIdFromEmail(email: String, password: String) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SEARCH_ID_BY_EMAIL_AND_PASSWORD)
        statement.setString(1, email)
        statement.setString(2, password)
        val resultSet = statement.executeQuery()
        return@withContext if (resultSet.next()) {
            resultSet.getInt("id")
        } else {
            null
        }
    }

    suspend fun create(clientSignUpModel: ClientSignUpModel) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_CLIENT, Statement.RETURN_GENERATED_KEYS)
        statement.setString(1, clientSignUpModel.name)
        statement.setString(2, clientSignUpModel.surname)
        statement.setString(3, clientSignUpModel.email)
        statement.setString(4, clientSignUpModel.phoneNumber)
        statement.setString(5, clientSignUpModel.password)
        statement.setString(6, clientSignUpModel.country)
        statement.setString(7, clientSignUpModel.city)
        statement.executeUpdate()
        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            return@withContext generatedKeys.getInt(1)
        } else {
            throw Exception("Unable to retrieve the id of the newly inserted Client")
        }
    }
    suspend fun read(id: Int) = withContext(Dispatchers.IO){
        val statement = connection.prepareStatement(SELECT_CLIENT_BY_ID)
        statement.setInt(1, id)
        val resultSet = statement.executeQuery()
        if (resultSet.next()) {
            return@withContext ClientEntity(
                id = resultSet.getInt("id"),
                image = resultSet.getString("image"),
                name = resultSet.getString("name"),
                surname = resultSet.getString("surname"),
                country = resultSet.getString("country"),
                city = resultSet.getString("city"),
                birthday = resultSet.getDate("birthday")?.toString()
            )
        } else {
            throw Exception("Record not found")
        }
    }
    suspend fun update(id: Int, updateClientModel: UpdateClientModel) = withContext(Dispatchers.IO){
        val statement = connection.prepareStatement(UPDATE_CLIENT_DATA)
        statement.setString(1, updateClientModel.name)
        statement.setString(2, updateClientModel.surname)
        statement.setDate(3, updateClientModel.birthday?.let { Date.valueOf(it) } )
        statement.setString(4, updateClientModel.country)
        statement.setString(5, updateClientModel.city)
        statement.setInt(6, id)
        statement.executeUpdate()
    }
}