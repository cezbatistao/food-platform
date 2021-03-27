package com.food.restaurant.domain

data class Category(
        val id: Long?,
        val code: String,
        val description: String
) {
    constructor(code: String, description: String) :
            this(null, code, description)
}
