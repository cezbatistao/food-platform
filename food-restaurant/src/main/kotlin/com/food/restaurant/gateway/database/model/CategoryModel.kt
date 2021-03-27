package com.food.restaurant.gateway.database.model

class CategoryModel(
        val id: Long?,
        val code: String,
        val description: String
) {
    constructor(code: String, description: String) :
            this(null, code, description)
}
