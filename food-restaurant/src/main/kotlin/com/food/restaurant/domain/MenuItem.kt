package com.food.restaurant.domain

import java.math.BigDecimal
import java.util.*

data class MenuItem(
        val id: Long?,
        val uuid: UUID?,
        val name: String,
        val description: String,
        val value: BigDecimal
) {
    constructor(name: String, description: String, value: BigDecimal) :
            this(null, null, name, description, value)
}
