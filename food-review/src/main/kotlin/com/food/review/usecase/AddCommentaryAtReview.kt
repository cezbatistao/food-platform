package com.food.review.usecase

import com.food.review.domain.Commentary
import com.food.review.domain.Review
import com.food.review.domain.ReviewStatus.VERIFIED
import com.food.review.gateway.ReviewGateway
import com.food.review.gateway.ReviewSendGateway
import org.springframework.stereotype.Component

@Component
class AddCommentaryAtReview(
    private val reviewGateway: ReviewGateway,
    private val reviewSendGateway: ReviewSendGateway
) {

    fun execute(commentary: Commentary) {
        val reviewSaved = this.addTextAndChangeStatusAtReview(commentary)
        reviewSendGateway.sendVerified(reviewSaved)
    }

    private fun addTextAndChangeStatusAtReview(commentary: Commentary): Review {
        val review = reviewGateway.findByUserUuidAndUuid(commentary.userUuid, commentary.uuid)
        val reviewWithCommentary = review.copy(text = commentary.text, status = VERIFIED)
        return reviewGateway.save(reviewWithCommentary)
    }
}
