package com.food.review.domain

import java.util.UUID

data class Order(
    val uuid: UUID,
    val userUuid: UUID,
    val restaurant: Restaurant,
    val items: List<OrderItem>)
