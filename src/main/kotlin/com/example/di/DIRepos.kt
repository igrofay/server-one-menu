package com.example.di

import com.example.data.repos.*
import com.example.domain.repos.*
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import org.kodein.di.new

val diRepos by DI.Module{
    bindProvider<AuthRestaurantRepos> { new(::AuthRestaurantReposImpl) }
    bindProvider<AuthClientRepos> { new(::AuthClientReposImpl) }
    bindProvider<RestaurantRepos> { RestaurantReposImpl(instance(),instance(),instance("images_path"))}
    bindProvider<MenuRestaurantRepos> { MenuRestaurantReposImpl(instance(), instance() ,instance("images_path"))}
    bindProvider<ClientRepos> { ClientReposImpl(instance(), instance(), instance("images_path")) }
    bindProvider<SupportRepos>{ new(::SupportReposImpl) }
}