package com.example.di

import io.ktor.server.application.*
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import java.sql.Connection
import java.sql.DriverManager

val diConnectionToDatabase by DI.Module{
    bindSingleton { connectToPostgres(instance(), true) }
}

private fun connectToPostgres(environment: ApplicationEnvironment, embedded: Boolean): Connection {
    Class.forName("org.postgresql.Driver")
    if (embedded) {
        return DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "root", "")
    } else {
        val url = environment.config.property("postgres.url").getString()
        val user = environment.config.property("postgres.user").getString()
        val password = environment.config.property("postgres.password").getString()
        return DriverManager.getConnection(url, user, password)
    }
}