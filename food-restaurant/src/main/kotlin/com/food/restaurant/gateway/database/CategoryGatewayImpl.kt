package com.food.restaurant.gateway.database

import com.food.restaurant.domain.Category
import com.food.restaurant.gateway.CategoryGateway

class CategoryGatewayImpl: CategoryGateway {

    override fun list(): List<Category> {
        return listOf()
    }
}