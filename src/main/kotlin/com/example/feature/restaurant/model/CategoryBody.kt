package com.example.feature.restaurant.model

import com.example.domain.model.menu.CategoryModel
import kotlinx.serialization.Serializable

@Serializable
data class CategoryBody(
    override val id: Int,
    override val name: String
) : CategoryModel{
    companion object{
        fun List<CategoryModel>.fromListModelToListBody() = this.map { it.fromModelToCategoryBodyBody() }
        private fun CategoryModel.fromModelToCategoryBodyBody() = CategoryBody(id, name)
    }
}
