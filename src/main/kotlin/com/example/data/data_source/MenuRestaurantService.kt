package com.example.data.data_source

import com.example.data.model.CategoryEntity
import com.example.data.model.DishEntity
import com.example.domain.model.menu.CreateDishModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.h2.jdbc.JdbcSQLSyntaxErrorException
import java.sql.Connection
import java.sql.Statement

class MenuRestaurantService(
    private val connection: Connection,
){
    companion object {
        private const val CREATE_TABLE_CATEGORISES =
            "CREATE TABLE CATEGORISES (ID SERIAL PRIMARY KEY, name VARCHAR(50), RestaurantId INTEGER, FOREIGN KEY (RestaurantId) REFERENCES RESTAURANTS (ID) ON DELETE CASCADE);"
        private const val CREATE_TABLE_DISH =
            "CREATE TABLE DISH (ID SERIAL PRIMARY KEY, image VARCHAR(30) NULL, name VARCHAR(50), description VARCHAR(250), components VARCHAR(100), CategoryId INTEGER, spicinessLevel INTEGER, price REAL, currencyCode VARCHAR(10), FOREIGN KEY (CategoryId) REFERENCES CATEGORISES (ID) ON DELETE CASCADE);"
        private const val SEARCH_RESTAURANT_CATEGORISES =
            "SELECT id, name FROM CATEGORISES WHERE RestaurantId = ?"
        private const val SEARCH_RESTAURANT_DISHES =
            "SELECT * FROM DISH WHERE CategoryId = ANY(?)"
        private const val SEARCH_CATEGORY =
            "SELECT * FROM CATEGORISES WHERE RestaurantId = ? AND name = ?"
        private const val INSERT_CATEGORY =
            "INSERT INTO CATEGORISES (name, restaurantId) VALUES (?, ?)"
        private const val UPDATE_CATEGORY_NAME =
            "UPDATE CATEGORISES SET name = ? WHERE id = ? AND RestaurantId = ?"
        private const val DELETE_CATEGORY =
            "DELETE FROM CATEGORISES WHERE id = ? AND RestaurantId = ?"
        private const val INSERT_DISH =
            "INSERT INTO DISH (name, description, components, categoryId, spicinessLevel, price, currencyCode) VALUES (?, ?, ?, ?, ?, ?, ?)"
        private const val SEARCH_ID_DISH =
            "SELECT id FROM DISH WHERE name = ? AND description = ? AND components = ? AND categoriesId = ? AND spicinessLevel = ? AND price = ? AND currencyCode = ?"
        private const val SEARCH_DISH_IMAGE =
            "SELECT image FROM DISH WHERE id = ?"
        private const val UPDATE_DISH_IMAGE =
            "UPDATE DISH SET image = ? WHERE id = ?"
        private const val UPDATE_PRICES_DISH =
            "UPDATE DISH SET price = ? WHERE id = ?"
    }
    init {
        val statement = connection.createStatement()
        try {
            statement.executeUpdate(CREATE_TABLE_CATEGORISES)
            statement.executeUpdate(CREATE_TABLE_DISH)
        } catch (e: JdbcSQLSyntaxErrorException) {
            println(e)
        }
    }

    suspend fun readCategories(restaurantId: Int) = withContext(Dispatchers.IO){
        val statement = connection.prepareStatement(SEARCH_RESTAURANT_CATEGORISES)
        statement.setInt(1, restaurantId)
        val answer = mutableListOf<CategoryEntity>()
        val resultSet = statement.executeQuery()
        while (resultSet.next()) {
            answer.add(
                CategoryEntity(
                    resultSet.getInt("id"),
                    resultSet.getString("name")
                )
            )
        }
        return@withContext answer
    }
    suspend fun createNewCategory(restaurantId: Int, category: String): Unit = withContext(Dispatchers.IO){
        val statement = connection.prepareStatement(INSERT_CATEGORY, Statement.RETURN_GENERATED_KEYS)
        statement.setString(1, category)
        statement.setInt(2, restaurantId)
        statement.executeUpdate()
    }
    suspend fun doesCategoryExist(restaurantId: Int, category: String) = withContext(Dispatchers.IO){
        val statement = connection.prepareStatement(SEARCH_CATEGORY)
        statement.setInt(1, restaurantId)
        statement.setString(2, category)
        val resultSet = statement.executeQuery()
        return@withContext resultSet.next()
    }
    suspend fun updateCategory(restaurantId: Int, category: String, categoryId: Int): Unit= withContext(Dispatchers.IO){
        val statement = connection.prepareStatement(UPDATE_CATEGORY_NAME)
        statement.setString(1, category)
        statement.setInt(2, categoryId)
        statement.setInt(3, restaurantId)
        statement.executeUpdate()
    }
    suspend fun deleteCategory(restaurantId: Int, categoryId: Int): Unit = withContext(Dispatchers.IO){
        val statement = connection.prepareStatement(DELETE_CATEGORY)
        statement.setInt(1, categoryId)
        statement.setInt(2, restaurantId)
        statement.executeUpdate()
    }
    suspend fun createDish(createDishModel: CreateDishModel) = withContext(Dispatchers.IO){
        val statement = connection.prepareStatement(INSERT_DISH, Statement.RETURN_GENERATED_KEYS)
        statement.setString(1, createDishModel.name)
        statement.setString(2, createDishModel.description)
        statement.setString(3, createDishModel.components)
        statement.setInt(4, createDishModel.categoryId)
        statement.setInt(5, createDishModel.spicinessLevel)
        statement.setFloat(6, createDishModel.price)
        statement.setString(7, createDishModel.currencyCode)
        statement.executeUpdate()

        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            return@withContext generatedKeys.getInt(1)
        } else {
            null
        }
    }
    suspend fun getDishId(createDishModel: CreateDishModel) = withContext(Dispatchers.IO){
        val statement = connection.prepareStatement(SEARCH_ID_DISH)
        statement.setString(1, createDishModel.name)
        statement.setString(2, createDishModel.description)
        statement.setString(3, createDishModel.components)
        statement.setInt(4, createDishModel.categoryId)
        statement.setInt(5, createDishModel.spicinessLevel)
        statement.setFloat(6, createDishModel.price)
        statement.setString(7, createDishModel.currencyCode)
        statement.executeQuery()
        val resultSet = statement.executeQuery()
        resultSet.getInt("id")
    }
    suspend fun getDishImage(dishId: Int) = withContext(Dispatchers.IO){
        val statement = connection.prepareStatement(SEARCH_DISH_IMAGE)
        statement.setInt(1, dishId)
        statement.executeQuery()
        val resultSet = statement.executeQuery()
        if (resultSet.next()){
            resultSet.getString("image")
        }else{
            null
        }
    }
    suspend fun updateDishImage(dishId: Int, nameImage: String) = withContext(Dispatchers.IO){
        val statement = connection.prepareStatement(UPDATE_DISH_IMAGE)
        statement.setString(1, nameImage)
        statement.setInt(2, dishId)
        statement.executeUpdate()
    }
    suspend fun getListDish(listCategoryId: List<Int>) = withContext(Dispatchers.IO){
        val statement = connection.prepareStatement(SEARCH_RESTAURANT_DISHES)
        val array = connection.createArrayOf("INTEGER", listCategoryId.toTypedArray())
        statement.setArray(1, array)
        val answer = mutableListOf<DishEntity>()
        val resultSet = statement.executeQuery()
        while (resultSet.next()){
            answer.add(
                DishEntity(
                    id = resultSet.getInt("id"),
                    image = resultSet.getString("image"),
                    name = resultSet.getString("name"),
                    description = resultSet.getString("description"),
                    components = resultSet.getString("components"),
                    categoriesId = resultSet.getInt("categoryId"),
                    spicinessLevel = resultSet.getInt("spicinessLevel"),
                    price = resultSet.getFloat("price"),
                    currency = resultSet.getString("currencyCode")
                )
            )
        }
        return@withContext answer
    }
    suspend fun updatePrice(listIdToPrice: List<Pair<Int, Float>>) = withContext(Dispatchers.IO){
        for(pair in listIdToPrice){
            val statement = connection.prepareStatement(UPDATE_PRICES_DISH)
            statement.setFloat(1, pair.second)
            statement.setInt(2, pair.first)
            statement.executeUpdate()
        }
    }
}