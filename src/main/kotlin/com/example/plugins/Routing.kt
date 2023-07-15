package com.example.plugins

import com.example.feature.auth.authRouting
import com.example.feature.client.clientRouting
import com.example.feature.restaurant.restaurantRouting
import com.example.feature.support.supportRouting
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import java.io.File

fun Application.configureRouting() {
    routing {
        supportRouting()
        authRouting()
        restaurantRouting()
        clientRouting()
        get("/images/{name}"){
            val name = call.parameters["name"] ?: call.respond(HttpStatusCode.BadRequest)
            val nameId = if (name.toString().contains('.'))
                name.toString().split('.').first()
            else name

            val imagesPath by closestDI().instance<String>("images_path")
            call.respondFile(File("$imagesPath\\$nameId.jpg"))
        }
    }
}
