package com.example.data.model

import com.example.domain.model.menu.CategoryModel

data class CategoryEntity(
    override val id: Int,
    override val name: String,
): CategoryModel
