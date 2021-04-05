package com.food.restaurant.domain

import java.util.*

data class Restaurant(
        val id: Long?,
        val uuid: UUID?,
        val name: String,
        val category: Category,
        val logo: String,
        val description: String,
        val address: String,
        val itens: List<MenuItem>
) {
    constructor(name: String, category: Category, logo: String,
                description: String, address: String, itens: List<MenuItem>) :
            this(null, null, name, category, logo, description, address, itens)
}
