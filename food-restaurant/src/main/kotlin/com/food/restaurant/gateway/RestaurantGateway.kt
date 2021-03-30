package com.food.restaurant.gateway

import com.food.restaurant.domain.Category
import com.food.restaurant.domain.Restaurant

interface RestaurantGateway {

    fun findAllByCategory(category: Category): List<Restaurant>

}
