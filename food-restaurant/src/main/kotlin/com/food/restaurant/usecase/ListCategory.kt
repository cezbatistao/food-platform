package com.food.restaurant.usecase

import com.food.restaurant.domain.Category
import com.food.restaurant.gateway.CategoryGateway
import org.springframework.stereotype.Component

@Component
class ListCategory(
        private val categoryGateway: CategoryGateway
) {

    fun execute(): List<Category> {
        return categoryGateway.list()
    }
}
