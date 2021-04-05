package com.food.restaurant.gateway

import com.food.restaurant.domain.Category
import com.food.restaurant.domain.Restaurant
import java.util.*

interface RestaurantGateway {

    fun findAllByCategory(category: Category): List<Restaurant>
    fun findByUuid(uuid: UUID): Restaurant

}
