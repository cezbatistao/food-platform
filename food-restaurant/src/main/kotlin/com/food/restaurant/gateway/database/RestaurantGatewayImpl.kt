package com.food.restaurant.gateway.database

import com.food.restaurant.domain.Category
import com.food.restaurant.domain.Restaurant
import com.food.restaurant.gateway.RestaurantGateway
import com.food.restaurant.gateway.database.repository.RestaurantRepository
import org.springframework.stereotype.Component

@Component
class RestaurantGatewayImpl(
        private val restaurantRepository: RestaurantRepository
): RestaurantGateway {

    override fun findAllByCategory(category: Category): List<Restaurant> {
        return restaurantRepository.findAllByCategory(category).map {
            Restaurant(
                    it.id,
                    it.name,
                    Category(it.category.id, it.category.code, it.category.description),
                    it.logo,
                    it.description,
                    it.address
            )
        }
    }
}
