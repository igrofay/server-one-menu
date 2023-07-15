package com.example.feature.restaurant.model

import com.example.domain.model.menu.DishModel
import kotlinx.serialization.Serializable

@Serializable
data class DishBody(
    override val id: Int,
    override val image: String?,
    override val name: String,
    override val description: String,
    override val components: String,
    override val categoriesId: Int,
    override val spicinessLevel: Int,
    override val price: Float,
    override val currency: String
) : DishModel {
    companion object {
        private fun DishModel.fromModelToDishBody() =
            DishBody(id, image, name, description, components, categoriesId, spicinessLevel, price, currency)

        fun List<DishModel>.fromListModelToListDishBody() = this.map { it.fromModelToDishBody() }
    }
}
