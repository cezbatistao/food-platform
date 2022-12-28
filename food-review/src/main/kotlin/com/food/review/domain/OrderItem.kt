package com.food.review.domain

import java.util.UUID

data class OrderItem(
    val uuid: UUID,
    val menuItemUuid: UUID,
    val name: String)
