package com.example.di

import com.example.domain.use_case.prices_change.PricesChangeUseCase
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.new

val diUseCase by DI.Module{
    bindProvider { new(::PricesChangeUseCase) }
}