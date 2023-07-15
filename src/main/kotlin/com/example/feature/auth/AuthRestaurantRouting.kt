package com.example.feature.auth

import com.example.domain.model.user.UserRole
import com.example.domain.repos.AuthRestaurantRepos
import com.example.feature.auth.model.PhoneBody
import com.example.feature.auth.model.ClientSignUpBody
import com.example.feature.auth.model.RestaurantSignInBody
import com.example.feature.auth.model.RestaurantSignUpBody
import com.example.feature.auth.utils.isEmailValid
import com.example.feature.auth.utils.isPhoneValid
import com.example.plugins.generateToken
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI


fun Route.authRestaurantRouting() {
    route("/restaurant") {
        post("/sign_in") {
            val body = call.receive<RestaurantSignInBody>()
            val authRestaurantRepos by closestDI().instance<AuthRestaurantRepos>()
            if (isPhoneValid(body.emailOrPhone)) {
                if (!authRestaurantRepos.checkIfPhoneNumberIsRegistered(body.emailOrPhone))
                    return@post call.respond(HttpStatusCode.NotFound)
                val id = authRestaurantRepos.getIdFromPhone(body.emailOrPhone, body.password)
                if (id != null) {
                    val token = this@route.generateToken(UserRole.Restaurant, id)
                    call.respond(hashMapOf("token" to token))
                } else {
                    call.respond(HttpStatusCode.BadRequest)
                }
            } else if (isEmailValid(body.emailOrPhone)) {
                if (!authRestaurantRepos.checkIfEmailIsRegistered(body.emailOrPhone))
                    return@post call.respond(HttpStatusCode.NotFound)
                val id = authRestaurantRepos.getIdFromEmail(body.emailOrPhone, body.password)
                if (id != null) {
                    val token = this@route.generateToken(UserRole.Restaurant, id)
                    call.respond(hashMapOf("token" to token))
                } else {
                    call.respond(HttpStatusCode.BadRequest)
                }
            } else {
                call.respond(HttpStatusCode.UnprocessableEntity)
            }
        }
        post("/sign_up/{verifierID?}") {
            // TODO доделать регистрацию через проверку номера
            val verifierID = call.parameters["verifierID"]
                ?: return@post call.respond(HttpStatusCode.BadRequest)
            val code = call.request.header("sms_code")
            if (code != "0000") return@post call.respond(HttpStatusCode.NotAcceptable)
            val restaurantSignInBody = call.receive<RestaurantSignUpBody>()
            val authRestaurantRepos by closestDI().instance<AuthRestaurantRepos>()
            if (authRestaurantRepos.checkIfEmailIsRegistered(restaurantSignInBody.email))
                return@post call.respond(HttpStatusCode.Conflict)
            val id = try {
                authRestaurantRepos.createRestaurant(restaurantSignInBody)
            }catch (e: Exception){
                authRestaurantRepos.getIdFromEmail(restaurantSignInBody.email, restaurantSignInBody.password)!!
            }
            val token = this@route.generateToken(UserRole.Restaurant, id)
            call.respond(hashMapOf("token" to token))
        }
        post("/phone_verification") {
            // TODO доделать регистрацию через проверку номера
            val phone = call.receive<PhoneBody>()
            val authRestaurantRepos by closestDI().instance<AuthRestaurantRepos>()
            if (authRestaurantRepos.checkIfPhoneNumberIsRegistered(phone.phoneNumber))
                return@post call.respond(HttpStatusCode.Conflict)
            call.respond(hashMapOf("verifierID" to "Wjhw29WQl"))

        }
    }
}
