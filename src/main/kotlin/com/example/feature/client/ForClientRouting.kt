package com.example.feature.client

import com.example.domain.model.user.UserRole
import com.example.domain.repos.ClientRepos
import com.example.domain.repos.RestaurantRepos
import com.example.feature.client.model.ClientBody.Companion.fromModelToClientBody
import com.example.feature.client.model.UpdateClientBody
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

fun Route.forClientRouting(){
    get("/profile"){
        val principal = call.principal<JWTPrincipal>()
        if (UserRole.valueOf(
                principal!!.payload.getClaim("role").asString()
            ) != UserRole.Client
        ) return@get call.respond(HttpStatusCode.BadRequest)
        val id = principal.payload.getClaim("id").asInt()
        val clientRepos by closestDI().instance<ClientRepos>()
        val clientModel = clientRepos.getProfile(id)
        call.respond(clientModel.fromModelToClientBody())
    }
    put("/profile"){
        val principal = call.principal<JWTPrincipal>()
        if (UserRole.valueOf(
                principal!!.payload.getClaim("role").asString()
            ) != UserRole.Client
        ) return@put call.respond(HttpStatusCode.BadRequest)
        val id = principal.payload.getClaim("id").asInt()
        val body = call.receive<UpdateClientBody>()
        val clientRepos by closestDI().instance<ClientRepos>()
        clientRepos.updateProfile(id, body)
        call.respond(HttpStatusCode.OK)
    }
    post("/upload_image"){
        val principal = call.principal<JWTPrincipal>()
        if (UserRole.valueOf(
                principal!!.payload.getClaim("role").asString()
            ) != UserRole.Client
        ) return@post call.respond(HttpStatusCode.BadRequest)
        val multipartData = call.receiveMultipart()
        val id = principal.payload.getClaim("id").asInt()
        val restaurantRepos by closestDI().instance<ClientRepos>()
        multipartData.forEachPart { part ->
            if (part is PartData.FileItem) {
                val fileBytes = part.streamProvider().readBytes()
                restaurantRepos.uploadImage(id, fileBytes)
            }
            part.dispose()
        }
        call.respond(HttpStatusCode.OK)
    }
}