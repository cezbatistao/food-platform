package com.food.review.domain

import java.math.BigDecimal
import java.util.UUID

data class OrderItem(
    val uuid: UUID,
    val menuItemUuid: UUID,
    val name: String,
    val amount: Int,
    val unitValue: BigDecimal)
