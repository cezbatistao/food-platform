package com.food.review.usecase

import com.food.review.domain.Review
import com.food.review.gateway.ReviewGateway
import java.util.UUID
import org.springframework.stereotype.Component

@Component
class FindReviewFromUserUuidByUuid(private val reviewGateway: ReviewGateway) {

    fun execute(userUuid: UUID, uuid: UUID): Review {
        return reviewGateway.findByUserUuidAndUuid(userUuid, uuid)
    }
}
