package com.example.plugins

import com.example.di.diModule
import io.ktor.server.application.*
import org.kodein.di.bindProvider
import org.kodein.di.ktor.di

fun Application.configureDI(){
    di {
        bindProvider { environment }
        import(diModule)
    }
}