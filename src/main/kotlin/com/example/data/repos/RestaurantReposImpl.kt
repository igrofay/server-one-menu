package com.example.data.repos

import com.example.data.data_source.RestaurantService
import com.example.domain.model.menu.InformationRestaurantModel
import com.example.domain.model.user.RestaurantModel
import com.example.domain.repos.RestaurantRepos
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class RestaurantReposImpl(
    private val restaurantService: RestaurantService,
    private val environment: ApplicationEnvironment,
    private val imagesPath: String,
): RestaurantRepos {
    override suspend fun getProfile(id: Int): RestaurantModel {
        val restaurantEntity = restaurantService.read(id)
        val issuer = environment.config.property("jwt.issuer").getString()
        return restaurantEntity.copy(
            image = restaurantEntity.image?.let {
                "$issuer/images/$it.jpg"
            }
        )
    }

    override suspend fun uploadImage(id: Int, image: ByteArray): Unit = withContext(Dispatchers.IO){
        restaurantService.getImageName(id)?.let { lastNameImage->
            File("$imagesPath\\$lastNameImage.jpg").delete()
        }
        val newName = System.currentTimeMillis().toString()
        File("$imagesPath\\$newName.jpg").writeBytes(image)
        restaurantService.updateImage(id, newName)
    }

    override suspend fun updateProfile(id: Int, name: String, address: String, description: String) {
        restaurantService.update(id, name, address, description)
    }
    override suspend fun getInformationRestaurant(restaurantId :Int) : InformationRestaurantModel? {
        val restaurantModel = restaurantService.getInformationRestaurant(restaurantId) ?: return null
        val issuer = environment.config.property("jwt.issuer").getString()
        return restaurantModel.copy(
            image = restaurantModel.image?.let {
                "$issuer/images/$it.jpg"
            }
        )
    }

}