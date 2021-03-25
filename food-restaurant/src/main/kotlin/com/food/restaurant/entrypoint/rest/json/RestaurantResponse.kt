package com.food.restaurant.entrypoint.rest.json

import java.util.*

data class RestaurantResponse(
        val uuid: UUID,
        val name: String,
        val category: CategoryResponse,
        val logo: String,
        val description: String,
        val address: String
) {
}
