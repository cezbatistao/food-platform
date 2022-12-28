package com.food.review.gateway

import com.food.review.domain.Review
import com.food.review.domain.ReviewPageable
import java.util.UUID
import org.springframework.data.domain.Page

interface ReviewGateway {

    fun save(review: Review): Review

    fun findByUserUuid(reviewPageable: ReviewPageable): Page<Review>

    fun findByUserUuidAndUuid(userUuid: UUID, uuid: UUID): Review

}
