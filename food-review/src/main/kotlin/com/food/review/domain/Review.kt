package com.food.review.domain

import com.food.review.config.annotation.Default
import java.time.LocalDateTime
import java.util.UUID

data class Review @Default constructor(
    val id: String?,
    val uuid: UUID,
    val status: ReviewStatus,
    val restaurant: Restaurant,
    val orderUuid: UUID,
    val userUuid: UUID,
    val items: List<OrderItem>,
    val text: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?) {

    constructor(status: ReviewStatus, restaurant: Restaurant, orderUuid: UUID, userUuid: UUID, items: List<OrderItem>) :
            this(null, UUID.randomUUID(), status, restaurant, orderUuid, userUuid, items, null, null, null)
}
