package com.food.restaurant.gateway.database.model

class RestaurantModel(
        val id: Long?,
        val name: String,
        val category: CategoryModel,
        val logo: String,
        val description: String,
        val address: String
) {
    constructor(name: String, category: CategoryModel, logo: String,
                description: String, address: String) :
            this(null, name, category, logo, description, address)
}
