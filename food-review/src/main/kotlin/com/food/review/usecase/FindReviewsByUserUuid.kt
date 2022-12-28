package com.food.review.usecase

import com.food.review.domain.Review
import com.food.review.domain.ReviewPageable
import com.food.review.gateway.ReviewGateway
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component

@Component
class FindReviewsByUserUuid(private val reviewGateway: ReviewGateway) {

    fun execute(reviewPageable: ReviewPageable): Page<Review> {
        return reviewGateway.findByUserUuid(reviewPageable)
    }
}
