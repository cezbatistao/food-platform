package com.food.review.domain

import com.food.review.config.annotation.Default
import java.time.LocalDateTime
import java.util.UUID

class Review @Default constructor(
    val id: String?,
    val uuid: UUID,
    val status: ReviewStatus,
    val order: Order,
    val text: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?) {

    constructor(status: ReviewStatus, order: Order) :
            this(null, UUID.randomUUID(), status, order, null, null, null)
}
