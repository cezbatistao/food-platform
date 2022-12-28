package com.food.review.usecase

import com.food.review.domain.Order
import com.food.review.domain.Review
import com.food.review.domain.ReviewStatus
import com.food.review.gateway.ReviewGateway
import com.food.review.gateway.ReviewSendGateway
import org.springframework.stereotype.Component

@Component
class CreateReview(
    private val reviewGateway: ReviewGateway,
    private val reviewSendGateway: ReviewSendGateway
) {

    fun execute(order: Order) {
        val review = Review(ReviewStatus.CREATED, order.restaurant, order.uuid, order.userUuid, order.items)
        val savedReview = this.reviewGateway.save(review)
        this.reviewSendGateway.sendCreated(savedReview)
    }
}
