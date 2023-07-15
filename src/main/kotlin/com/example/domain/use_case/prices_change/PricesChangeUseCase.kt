package com.example.domain.use_case.prices_change

import com.example.domain.model.price_change.PriceChangeAction
import com.example.domain.model.price_change.PricesChangeModel
import com.example.domain.model.price_change.PricesChangeType
import com.example.domain.repos.MenuRestaurantRepos
import kotlin.math.pow
import kotlin.math.roundToInt

class PricesChangeUseCase (
    private val menuRestaurantRepos: MenuRestaurantRepos,
) {
    suspend fun execute(pricesChangeModel: PricesChangeModel){
        val listIdToPrice = menuRestaurantRepos.getPricesDish(pricesChangeModel.categoriesId)
        val newPrices = mutableListOf<Pair<Int, Float>>()
        for (pair in listIdToPrice){
            val value = when(pricesChangeModel.priceChangeType){
                PricesChangeType.Percent -> pricesChangeModel
                    .priceChangeAction
                    .calculateValuePercent(pair.second, pricesChangeModel.calculationValue)
            }
            val roundValue = (value * (10.0).pow(pricesChangeModel.priceFormat)).roundToInt() / (10.0).pow(pricesChangeModel.priceFormat)
            newPrices.add(pair.first to roundValue.toFloat())
        }
        menuRestaurantRepos.updatePricesDish(newPrices)
    }
    private fun PriceChangeAction.calculateValuePercent(value: Float, percentValue: Float) : Float{
        return when(this){
            PriceChangeAction.Increase -> value + (value percent percentValue)
            PriceChangeAction.Decrease -> value - (value percent percentValue)
        }
    }
    private infix fun Float.percent(value: Float) : Float{
        return this/100 * value
    }
}