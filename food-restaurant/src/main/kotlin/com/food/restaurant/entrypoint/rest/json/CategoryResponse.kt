package com.food.restaurant.entrypoint.rest.json

import java.util.*

data class CategoryResponse(
        val uuid: UUID,
        val code: String,
        val description: String
) {
}