package com.food.restaurant.usecase.exception

class ValidationException(
        val code: String,
        val error: String,
        private val description: String,
        val messageArgs: List<String>
): RuntimeException(description) {

    constructor(code: String, error: String, description: String) :
        this(code, error, description, listOf())

}
