package com.food.restaurant.usecase

import com.food.restaurant.domain.Category
import com.food.restaurant.gateway.CategoryGateway

class ListCategory(
        private val categoryGateway: CategoryGateway
) {

    fun execute(): List<Category> {
        return categoryGateway.list()
    }
}
