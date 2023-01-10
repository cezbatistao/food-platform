package com.food.review.domain.exception

class EntityNotFoundException(
        val code: String,
        val error: String,
        private val description: String
): RuntimeException(description)
