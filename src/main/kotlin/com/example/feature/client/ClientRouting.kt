package com.example.feature.client

import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.clientRouting(){
    route("/client"){
        authenticate("auth-jwt") {
            forClientRouting()
        }
    }

}