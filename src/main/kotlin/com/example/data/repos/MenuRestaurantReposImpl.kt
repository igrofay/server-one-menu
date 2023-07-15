package com.example.data.repos

import com.example.data.data_source.MenuRestaurantService
import com.example.domain.model.menu.CategoryModel
import com.example.domain.model.menu.CreateDishModel
import com.example.domain.model.menu.DishModel
import com.example.domain.repos.MenuRestaurantRepos
import io.ktor.server.application.*
import java.io.File

class MenuRestaurantReposImpl(
    private val menuRestaurantService: MenuRestaurantService,
    private val environment: ApplicationEnvironment,
    private val imagesPath: String,
) : MenuRestaurantRepos {
    override suspend fun getCategories(restaurantId: Int): List<CategoryModel> {
        return menuRestaurantService.readCategories(restaurantId)
    }

    override suspend fun getListDish(listCategoryId: List<CategoryModel>): List<DishModel> {
        val issuer = environment.config.property("jwt.issuer").getString()
        return menuRestaurantService.getListDish(listCategoryId.map { it.id }).map {dishEntity ->
            dishEntity.copy(
                image = dishEntity.image?.let {
                    "$issuer/images/$it.jpg"
                }
            )
        }
    }


    override suspend fun createNewCategory(restaurantId: Int, category: String): Boolean {
        return if (menuRestaurantService.doesCategoryExist(restaurantId, category)){
            false
        }else{
            menuRestaurantService.createNewCategory(restaurantId, category)
            true
        }
    }

    override suspend fun updateCategory(restaurantId: Int, categoryId: Int, category: String): Boolean {
        return if (menuRestaurantService.doesCategoryExist(restaurantId, category)){
            false
        }else{
            menuRestaurantService.updateCategory(restaurantId, category, categoryId)
            true
        }
    }

    override suspend fun deleteCategory(restaurantId: Int, categoryId: Int) {
        menuRestaurantService.deleteCategory(restaurantId, categoryId)
    }

    override suspend fun createDish(createDishModel: CreateDishModel): Int {
        return menuRestaurantService.createDish(createDishModel)
            ?: menuRestaurantService.getDishId(createDishModel)
    }

    override suspend fun updateDishImage(dishId: Int, image: ByteArray) {
        menuRestaurantService.getDishImage(dishId)?.let {lastNameImage->
            File("$imagesPath${File.separatorChar}$lastNameImage.jpg").delete()
        }
        val newName = System.currentTimeMillis().toString()
        File("$imagesPath${File.separatorChar}$newName.jpg").writeBytes(image)
        menuRestaurantService.updateDishImage(dishId, newName)
    }

    override suspend fun getPricesDish(listCategoryId: List<Int>): List<Pair<Int, Float>> {
        return menuRestaurantService.getListDish(listCategoryId).map { it.id to it.price }
    }

    override suspend fun updatePricesDish(listIdToPrice: List<Pair<Int, Float>>) {
        menuRestaurantService.updatePrice(listIdToPrice)
    }

}