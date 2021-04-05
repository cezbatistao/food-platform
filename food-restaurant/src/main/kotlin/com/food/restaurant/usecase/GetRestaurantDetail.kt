package com.food.restaurant.usecase

import com.food.restaurant.domain.Restaurant
import com.food.restaurant.gateway.RestaurantGateway
import org.springframework.stereotype.Component
import java.util.*

@Component
class GetRestaurantDetail(
        private val restaurantGateway: RestaurantGateway
) {

    fun execute(uuid: UUID): Restaurant {
        return this.restaurantGateway.findByUuid(uuid)
    }
}
