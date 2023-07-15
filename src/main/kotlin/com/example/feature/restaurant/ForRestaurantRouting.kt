package com.example.feature.restaurant

import com.example.domain.model.user.UserRole
import com.example.domain.repos.RestaurantRepos
import com.example.feature.restaurant.model.ProfileRestaurantBody.Companion.fromModelToProfileRestaurantBody
import com.example.feature.restaurant.model.UpdateRestaurantBody
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI


fun Route.forRestaurantRouting() {
    get("/profile") {
        val principal = call.principal<JWTPrincipal>()
        if (UserRole.valueOf(
                principal!!.payload.getClaim("role").asString()
            ) != UserRole.Restaurant
        ) return@get call.respond(HttpStatusCode.Unauthorized)
        val id = principal.payload.getClaim("id").asInt()
        val restaurantRepos by closestDI().instance<RestaurantRepos>()
        val restaurantModel = restaurantRepos.getProfile(id)
        call.respond(restaurantModel.fromModelToProfileRestaurantBody())
    }
    post("/upload_image"){
        val principal = call.principal<JWTPrincipal>()
        if (UserRole.valueOf(
                principal!!.payload.getClaim("role").asString()
            ) != UserRole.Restaurant
        ) return@post call.respond(HttpStatusCode.Unauthorized)
        val multipartData = call.receiveMultipart()
        val id = principal.payload.getClaim("id").asInt()
        val restaurantRepos by closestDI().instance<RestaurantRepos>()
        multipartData.forEachPart { part ->
            if (part is PartData.FileItem) {
                val fileBytes = part.streamProvider().readBytes()
                restaurantRepos.uploadImage(id, fileBytes)
            }
            part.dispose()
        }
        call.respond(HttpStatusCode.OK)
    }
    put("/profile"){
        val principal = call.principal<JWTPrincipal>()
        if (UserRole.valueOf(
                principal!!.payload.getClaim("role").asString()
            ) != UserRole.Restaurant
        ) return@put call.respond(HttpStatusCode.Unauthorized)
        val body = call.receive<UpdateRestaurantBody>()
        val id = principal.payload.getClaim("id").asInt()
        val restaurantRepos by closestDI().instance<RestaurantRepos>()
        restaurantRepos.updateProfile(id, body.name, body.address, body.description)
        call.respond(HttpStatusCode.OK)
    }
}