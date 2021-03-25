package com.food.restaurant.gateway

import com.food.restaurant.domain.Category

interface CategoryGateway {

    fun list(): List<Category>
    fun findByCode(category: String): Category

}
