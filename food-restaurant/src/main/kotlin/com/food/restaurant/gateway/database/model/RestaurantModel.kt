package com.food.restaurant.gateway.database.model

class RestaurantModel(
        val id: Long?,
        val uuid: String,
        val name: String,
        val category: CategoryModel,
        val logo: String,
        val description: String,
        val address: String,
        val itens: List<MenuItemModel>?
) {

    constructor(uuid: String, name: String, category: CategoryModel, logo: String,
                description: String, address: String, itens: List<MenuItemModel>) :
            this(null, uuid, name, category, logo, description, address, itens)

    constructor(uuid: String, name: String, category: CategoryModel, logo: String,
                description: String, address: String) :
            this(null, uuid, name, category, logo, description, address, listOf())
}
