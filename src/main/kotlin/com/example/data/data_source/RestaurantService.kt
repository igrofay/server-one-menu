package com.example.data.data_source

import com.example.data.model.InformationRestaurantEntity
import com.example.data.model.RestaurantEntity
import com.example.domain.model.auth.RestaurantSignUpModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.h2.jdbc.JdbcSQLSyntaxErrorException
import java.sql.Connection
import java.sql.Statement

class RestaurantService(
    private val connection: Connection,
) {
    companion object {
        private const val CREATE_TABLE_RESTAURANTS =
            "CREATE TABLE RESTAURANTS (ID SERIAL PRIMARY KEY, IMAGE VARCHAR(30) NULL, NAME VARCHAR(100), ADDRESS VARCHAR(150), DESCRIPTION VARCHAR(800), EMAIL VARCHAR(80), PHONE VARCHAR(25), PASSWORD VARCHAR(64));"
        private const val SEARCH_PHONE_NUMBER =
            "SELECT phone FROM RESTAURANTS WHERE phone = ?;"
        private const val SEARCH_EMAIL =
            "SELECT email FROM RESTAURANTS WHERE email = ?;"
        private const val INSERT_RESTAURANT =
            "INSERT INTO RESTAURANTS (name, address, description, email, phone, password) VALUES (?, ?, ?, ?, ?, ?)"
        private const val SELECT_RESTAURANT_BY_ID =
            "SELECT id, image, name, address, description, email, phone FROM RESTAURANTS WHERE id = ?"
        private const val SEARCH_ID_BY_EMAIL_AND_PASSWORD =
            "SELECT id FROM RESTAURANTS WHERE email = ? AND password = ?;"
        private const val SEARCH_ID_BY_PHONE_AND_PASSWORD =
            "SELECT id FROM RESTAURANTS WHERE phone = ? AND password = ?;"
        private const val UPDATE_RESTAURANT_IMAGE =
            "UPDATE RESTAURANTS SET image = ? WHERE id = ?"
        private const val SEARCH_RESTAURANT_IMAGE =
            "SELECT image FROM RESTAURANTS WHERE id = ?;"
        private const val UPDATE_RESTAURANT_DATA =
            "UPDATE RESTAURANTS SET name = ?, address = ?, description = ? WHERE id = ?"
        private const val SEARCH_INFORMATION_RESTAURANT =
            "SELECT name, description, image FROM RESTAURANTS WHERE id = ?"
    }

    init {
        val statement = connection.createStatement()
        try {
            statement.executeUpdate(CREATE_TABLE_RESTAURANTS)
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

    suspend fun create(restaurantSignUpModel: RestaurantSignUpModel) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_RESTAURANT, Statement.RETURN_GENERATED_KEYS)
        statement.setString(1, restaurantSignUpModel.name)
        statement.setString(2, restaurantSignUpModel.address)
        statement.setString(3, restaurantSignUpModel.description)
        statement.setString(4, restaurantSignUpModel.email)
        statement.setString(5, restaurantSignUpModel.phoneNumber)
        statement.setString(6, restaurantSignUpModel.password)
        statement.executeUpdate()
        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            return@withContext generatedKeys.getInt(1)
        } else {
            throw Exception("Unable to retrieve the id of the newly inserted Restaurant")
        }
    }

    suspend fun read(id: Int) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_RESTAURANT_BY_ID)
        statement.setInt(1, id)
        val resultSet = statement.executeQuery()
        if (resultSet.next()) {
            return@withContext RestaurantEntity(
                name = resultSet.getString("name"),
                address = resultSet.getString("address"),
                description = resultSet.getString("description"),
                email = resultSet.getString("email"),
                phone = resultSet.getString("phone"),
                id = resultSet.getInt("id"),
                image = resultSet.getString("image")
            )
        } else {
            throw Exception("Record not found")
        }
    }

    suspend fun getImageName(id: Int) = withContext(Dispatchers.IO){
        val statement = connection.prepareStatement(SEARCH_RESTAURANT_IMAGE)
        statement.setInt(1, id)
        val resultSet = statement.executeQuery()
        return@withContext if (resultSet.next()) {
            resultSet.getString("image")
        } else {
            null
        }
    }
    suspend fun updateImage(id:Int, name: String) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_RESTAURANT_IMAGE)
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
    suspend fun update(id: Int, name: String, address: String, description: String) = withContext(Dispatchers.IO){
        val statement = connection.prepareStatement(UPDATE_RESTAURANT_DATA)
        statement.setString(1, name)
        statement.setString(2, address)
        statement.setString(3, description)
        statement.setInt(4, id)
        statement.executeUpdate()
    }
    suspend fun getInformationRestaurant(id: Int) = withContext(Dispatchers.IO){
        val statement = connection.prepareStatement(SEARCH_INFORMATION_RESTAURANT)
        statement.setInt(1, id)
        val resultSet = statement.executeQuery()
        return@withContext if (resultSet.next()) {
            InformationRestaurantEntity(
                image = resultSet.getString("image"),
                name = resultSet.getString("name"),
                description = resultSet.getString("description")
            )
        } else {
            null
        }
    }
}