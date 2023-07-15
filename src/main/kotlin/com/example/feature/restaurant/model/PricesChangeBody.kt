package com.example.feature.restaurant.model

import com.example.domain.model.price_change.PriceChangeAction
import com.example.domain.model.price_change.PricesChangeModel
import com.example.domain.model.price_change.PricesChangeType
import kotlinx.serialization.Serializable

@Serializable
data class PricesChangeBody(
    override val categoriesId: List<Int>,
    override val priceChangeAction: PriceChangeAction,
    override val priceChangeType: PricesChangeType,
    override val calculationValue: Float,
    override val priceFormat: Int
) : PricesChangeModel
