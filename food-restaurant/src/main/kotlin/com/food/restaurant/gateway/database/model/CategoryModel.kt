package com.food.restaurant.gateway.database.model

class CategoryModel(
        val id: Long?,
        val uuid: String,
        val code: String,
        val description: String
) {
    constructor(uuid: String, code: String, description: String) :
            this(null, uuid, code, description)
}
