package com.food.restaurant.entrypoint.rest.json

data class RestaurantResponse(
        val id: Long,
        val name: String,
        val category: CategoryResponse,
        val logo: String,
        val description: String,
        val address: String
) {
}
