package com.example.feature.restaurant

import com.example.domain.model.user.UserRole
import com.example.domain.repos.MenuRestaurantRepos
import com.example.feature.restaurant.model.CategoryBody
import com.example.feature.restaurant.model.CategoryBody.Companion.fromListModelToListBody
import com.example.feature.restaurant.model.CreateCategoryBody
import com.example.feature.restaurant.model.CreateDishBody
import com.example.feature.restaurant.model.DishBody.Companion.fromListModelToListDishBody
import com.example.feature.restaurant.model.MenuRestaurantBody
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

fun Route.menuRestaurantRouting(){
    get("/menu/{restaurantId?}"){
        val principal = call.principal<JWTPrincipal>()
        val restaurantId =if (UserRole.valueOf(
                principal!!.payload.getClaim("role").asString()
            ) == UserRole.Restaurant
        ){
            principal.payload.getClaim("id").asInt()
        }else{
            call.parameters["restaurantId"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
        }
        val menuRestaurantRepos by closestDI().instance<MenuRestaurantRepos>()
        val categories = menuRestaurantRepos.getCategories(restaurantId)
        val dishes = menuRestaurantRepos.getListDish(categories)
        call.respond(
            MenuRestaurantBody(
                categories.fromListModelToListBody(),
                dishes.fromListModelToListDishBody(),
            )
        )
    }
    get("/category") {
        val principal = call.principal<JWTPrincipal>()
        if (UserRole.valueOf(
                principal!!.payload.getClaim("role").asString()
            ) != UserRole.Restaurant
        ) return@get call.respond(HttpStatusCode.Unauthorized)
        val id = principal.payload.getClaim("id").asInt()
        val menuRestaurantRepos by closestDI().instance<MenuRestaurantRepos>()
        val categories = menuRestaurantRepos.getCategories(id)
        call.respond(categories.fromListModelToListBody())
    }
    post("/category"){
        val principal = call.principal<JWTPrincipal>()
        if (UserRole.valueOf(
                principal!!.payload.getClaim("role").asString()
            ) != UserRole.Restaurant
        ) return@post call.respond(HttpStatusCode.Unauthorized)
        val restaurantId = principal.payload.getClaim("id").asInt()
        val body = call.receive<CreateCategoryBody>()
        val menuRestaurantRepos by closestDI().instance<MenuRestaurantRepos>()
        if (menuRestaurantRepos.createNewCategory(restaurantId, body.category)){
            call.respond(HttpStatusCode.Created)
        }else{
            call.respond(HttpStatusCode.Conflict)
        }
    }
    put("/category"){
        val principal = call.principal<JWTPrincipal>()
        if (UserRole.valueOf(
                principal!!.payload.getClaim("role").asString()
            ) != UserRole.Restaurant
        ) return@put call.respond(HttpStatusCode.Unauthorized)
        val restaurantId = principal.payload.getClaim("id").asInt()
        val categoryBody = call.receive<CategoryBody>()
        val menuRestaurantRepos by closestDI().instance<MenuRestaurantRepos>()
        if (menuRestaurantRepos.updateCategory(restaurantId, categoryBody.id, categoryBody.name)){
            call.respond(HttpStatusCode.Accepted)
        }else{
            call.respond(HttpStatusCode.Conflict)
        }
    }
    delete("/category/{id}"){
        val principal = call.principal<JWTPrincipal>()
        if (UserRole.valueOf(
                principal!!.payload.getClaim("role").asString()
            ) != UserRole.Restaurant
        ) return@delete call.respond(HttpStatusCode.Unauthorized)
        val restaurantId = principal.payload.getClaim("id").asInt()
        val categoryId = call.parameters["id"]?.toInt() ?: return@delete call.respond(HttpStatusCode.BadRequest)
        val menuRestaurantRepos by closestDI().instance<MenuRestaurantRepos>()
        menuRestaurantRepos.deleteCategory(restaurantId, categoryId)
        call.respond(HttpStatusCode.OK)
    }
    post("/dish") {
        val principal = call.principal<JWTPrincipal>()
        if (UserRole.valueOf(
                principal!!.payload.getClaim("role").asString()
            ) != UserRole.Restaurant
        ) return@post call.respond(HttpStatusCode.Unauthorized)
//        val restaurantId = principal.payload.getClaim("id").asInt()
        val createDishBody = call.receive<CreateDishBody>()
        val menuRestaurantRepos by closestDI().instance<MenuRestaurantRepos>()
        val dishId = menuRestaurantRepos.createDish(createDishBody)
        call.respond(hashMapOf("id" to dishId))
    }
    post("/dish/upload_image/{id}") {
        val principal = call.principal<JWTPrincipal>()
        if (UserRole.valueOf(
                principal!!.payload.getClaim("role").asString()
            ) != UserRole.Restaurant
        ) return@post call.respond(HttpStatusCode.Unauthorized)
        val dishId = call.parameters["id"]?.toInt() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val multipartData = call.receiveMultipart()
        val menuRestaurantRepos by closestDI().instance<MenuRestaurantRepos>()
        multipartData.forEachPart { part ->
            if (part is PartData.FileItem) {
                val fileBytes = part.streamProvider().readBytes()
                menuRestaurantRepos.updateDishImage(dishId, fileBytes)
            }
            part.dispose()
        }
        call.respond(HttpStatusCode.OK)
    }
}