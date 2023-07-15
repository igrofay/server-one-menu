package com.example.feature.restaurant

import com.example.domain.repos.RestaurantRepos
import com.example.feature.restaurant.model.InformationRestaurantBody.Companion.fromModelToInformationRestaurantBody
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Routing.restaurantRouting(){
    route("/restaurant"){
        authenticate("auth-jwt") {
            forRestaurantRouting()
            menuRestaurantRouting()
            priceChangeRouting()
            get("/information/{id}"){
                val restaurantId = call.parameters["id"]?.toInt()
                    ?: return@get call.respond(HttpStatusCode.BadRequest)
                val restaurantRepos by closestDI().instance<RestaurantRepos>()
                val informationRestaurantModel = restaurantRepos.getInformationRestaurant(restaurantId)
                if (informationRestaurantModel == null){
                    call.respond(HttpStatusCode.NotFound)
                }else{
                    call.respond(informationRestaurantModel.fromModelToInformationRestaurantBody())
                }
            }
        }
    }
}