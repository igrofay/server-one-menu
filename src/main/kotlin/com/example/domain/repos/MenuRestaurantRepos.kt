package com.example.domain.repos

import com.example.domain.model.menu.CategoryModel
import com.example.domain.model.menu.CreateDishModel
import com.example.domain.model.menu.DishModel

interface MenuRestaurantRepos {
    suspend fun getCategories(restaurantId: Int) : List<CategoryModel>
    suspend fun getListDish(listCategoryId: List<CategoryModel>) : List<DishModel>
    suspend fun createNewCategory(restaurantId: Int, category: String) : Boolean
    suspend fun updateCategory(restaurantId: Int, categoryId: Int, category: String) : Boolean
    suspend fun deleteCategory(restaurantId: Int, categoryId: Int,)
    suspend fun createDish(createDishModel: CreateDishModel) : Int
    suspend fun updateDishImage(dishId: Int, image: ByteArray)
    suspend fun getPricesDish(listCategoryId: List<Int>) : List<Pair<Int, Float>> // id, price
    suspend fun updatePricesDish(listIdToPrice: List<Pair<Int, Float>>)
}