package com.example.feature.auth

import io.ktor.server.routing.*


fun Routing.authRouting(){
    route("/auth"){
        authRestaurantRouting()
        authClientRouting()
    }
}