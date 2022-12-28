package com.food.restaurant.domain.exception

class EntityNotFoundException(
        val code: String,
        val error: String,
        private val description: String
): RuntimeException(description) {
}
