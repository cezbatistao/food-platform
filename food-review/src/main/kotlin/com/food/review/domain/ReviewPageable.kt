package com.food.review.domain

import java.util.UUID

data class ReviewPageable (
    val userUuid: UUID,
    val restaurantUuid: UUID?,
    val status: ReviewStatus?,
    val page: Int,
    val size: Int
)
