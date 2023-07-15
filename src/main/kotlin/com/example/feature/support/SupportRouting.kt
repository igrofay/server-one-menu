package com.example.feature.support

import com.example.domain.model.user.UserRole
import com.example.domain.repos.SupportRepos
import com.example.feature.support.model.CreateMessageDataBody
import com.example.feature.support.model.MessageDataBody.Companion.fromListModelToListMessageData
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Route.supportRouting() {
    authenticate("auth-jwt") {
        get("/support") {
            val principal = call.principal<JWTPrincipal>()
            val id = principal!!.payload.getClaim("id").asInt()
            val role = UserRole.valueOf(
                principal.payload.getClaim("role").asString()
            )
            val supportRepos by closestDI().instance<SupportRepos>()
            val answer = supportRepos.getCorrespondence(id, role)
            call.respond(answer.fromListModelToListMessageData())
        }
        post("/support") {
            val principal = call.principal<JWTPrincipal>()
            val id = principal!!.payload.getClaim("id").asInt()
            val role = UserRole.valueOf(
                principal.payload.getClaim("role").asString()
            )
            val body = call.receive<CreateMessageDataBody>()
            val supportRepos by closestDI().instance<SupportRepos>()
            val answer = supportRepos.addMessage(id, role, body.text, body.date)
            call.respond(answer.fromListModelToListMessageData())
        }
    }

}