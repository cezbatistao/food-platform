package com.food.restaurant.domain

import java.util.*

data class Category(
        val id: Long?,
        val uuid: UUID?,
        val code: String,
        val description: String
) {
    constructor(code: String, description: String) :
            this(null, null, code, description)
}
