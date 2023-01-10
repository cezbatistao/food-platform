package com.food.review.usecase

import com.food.review.domain.Order
import com.food.review.domain.Review
import com.food.review.domain.ReviewStatus
import com.food.review.domain.exception.IsNotTheSameOrderException
import com.food.review.gateway.OrderGateway
import com.food.review.gateway.ReviewGateway
import com.food.review.gateway.ReviewSendGateway
import org.springframework.stereotype.Component

@Component
class CreateReview(
    private val reviewGateway: ReviewGateway,
    private val reviewSendGateway: ReviewSendGateway,
    private val orderGateway: OrderGateway
) {

    fun execute(order: Order) {
        val orderFound = orderGateway.findByUserUuidAndUuid(order.userUuid, order.uuid)

        if(order != orderFound) {
            throw IsNotTheSameOrderException("0002", "isNotTheSameOrderException", "Order ")
        }

        val review = Review(ReviewStatus.CREATED, order.restaurant, order.uuid, order.userUuid, order.items)
        val savedReview = this.reviewGateway.save(review)
        this.reviewSendGateway.sendCreated(savedReview)
    }
}
