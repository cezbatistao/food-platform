package com.food.restaurant.usecase

import com.food.restaurant.domain.Category
import com.food.restaurant.domain.Restaurant
import com.food.restaurant.gateway.CategoryGateway
import com.food.restaurant.gateway.RestaurantGateway
import com.food.restaurant.usecase.exception.ValidationException
import org.springframework.stereotype.Component

@Component
class FindRestaurantsByCategory(
        private val restaurantGateway: RestaurantGateway,
        private val categoryGateway: CategoryGateway
) {

    fun execute(category: String): List<Restaurant> {
        val categoryReturn: Category? = categoryGateway.findByCode(category)

        if(categoryReturn == null) {
            val validationException = ValidationException("1000", "validationException", "Category ${category} don't exists")
            throw validationException
        }

        return restaurantGateway.findAllByCategory(categoryReturn)
    }
}
