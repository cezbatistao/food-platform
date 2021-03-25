package com.food.restaurant.entrypoint.rest.json

import java.math.BigDecimal
import java.util.*

data class MenuItemResponse(
        val uuid: UUID,
        val name: String,
        val description: String,
        val value: String
) {
}
