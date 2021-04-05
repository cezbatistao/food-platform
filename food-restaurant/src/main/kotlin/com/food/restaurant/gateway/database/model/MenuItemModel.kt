package com.food.restaurant.gateway.database.model

import java.math.BigDecimal
import java.util.*

class MenuItemModel(
        val id: Long?,
        val uuid: String,
        val name: String,
        val description: String,
        val value: BigDecimal
) {
    constructor(uuid: String, name: String, description: String, value: BigDecimal) :
            this(null, uuid, name, description, value)
}
