package com.example.domain.model.price_change

interface PricesChangeModel {
    val categoriesId: List<Int>
    val priceChangeAction: PriceChangeAction
    val priceChangeType: PricesChangeType
    val calculationValue: Float
    val priceFormat: Int
}