package com.food.review.domain.exception

class IsNotTheSameOrderException(
    val code: String,
    val error: String,
    private val description: String
): RuntimeException(description)
