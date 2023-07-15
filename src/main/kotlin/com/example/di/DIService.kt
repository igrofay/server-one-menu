package com.example.di

import com.example.data.data_source.ClientService
import com.example.data.data_source.MenuRestaurantService
import com.example.data.data_source.RestaurantService
import com.example.data.data_source.SupportService
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.new

val diService by DI.Module{
    bindSingleton { new(::RestaurantService) }
    bindSingleton { new(::MenuRestaurantService) }
    bindSingleton { new(::ClientService)  }
    bindSingleton { new(::SupportService) }
}