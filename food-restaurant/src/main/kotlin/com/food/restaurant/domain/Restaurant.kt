package com.food.restaurant.domain

data class Restaurant(
        val id: Long?,
        val name: String,
        val category: Category,
        val logo: String,
        val description: String,
        val address: String
) {
    constructor(name: String, category: Category, logo: String,
                description: String, address: String) :
            this(null, name, category, logo, description, address)
}
