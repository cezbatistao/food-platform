package com.food.restaurant.entrypoint.rest.json

import java.util.*

data class RestaurantDetailResponse(
        val uuid: UUID,
        val name: String,
        val category: CategoryResponse,
        val logo: String,
        val description: String,
        val address: String,
        val itens: List<MenuItemResponse>
) {
}
