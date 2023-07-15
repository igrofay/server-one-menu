package com.example.feature.restaurant

import com.example.domain.model.user.UserRole
import com.example.domain.use_case.prices_change.PricesChangeUseCase
import com.example.feature.restaurant.model.PricesChangeBody
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Route.priceChangeRouting(){
    put("/prices_change") {
        val principal = call.principal<JWTPrincipal>()
        if (UserRole.valueOf(
                principal!!.payload.getClaim("role").asString()
            ) != UserRole.Restaurant
        ) return@put call.respond(HttpStatusCode.Unauthorized)
        val body = call.receive<PricesChangeBody>()
        val pricesChangeUseCase by closestDI().instance<PricesChangeUseCase>()
        pricesChangeUseCase.execute(body)
        call.respond(HttpStatusCode.OK)
    }
}