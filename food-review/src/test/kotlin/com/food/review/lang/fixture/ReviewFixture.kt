package com.food.review.lang.fixture

import com.food.review.domain.Order
import com.food.review.domain.Review
import com.food.review.domain.ReviewStatus.CREATED
import java.time.LocalDateTime
import java.util.UUID

class ReviewFixture {

    companion object {
        fun getValidCreated(): Review {
            val orderValid = OrderFixture.getValid()
            return Review(
                CREATED, orderValid.restaurant, orderValid.uuid, orderValid.userUuid, orderValid.items
            )
        }

        fun getValidCreatedSaved(): Review {
            val orderValid = OrderFixture.getValid()
            return getValidCreatedSaved("63aae35dcf1dd00171077748", orderValid)
        }

        fun getValidCreatedSaved(reviewId: String, order: Order): Review {
            return Review(
                reviewId,
                UUID.fromString("f58ceb5b-6e70-489c-907b-0efe3b2feca5"),
                CREATED,
                order.restaurant,
                order.uuid,
                order.userUuid,
                order.items,
                "",
                LocalDateTime.of(2022, 12, 7, 20, 21, 45),
                LocalDateTime.of(2022, 12, 7, 20, 21, 46)
            )
        }

        fun getListValidCreatedSaved(): List<Review> {
            val orderValid = OrderFixture.getValid()
            return arrayListOf(
                getValidCreatedSaved("63aae35dcf1dd00171077748", orderValid),
                getValidCreatedSaved("63ada10009d6ae745db7ff52", orderValid),
                getValidCreatedSaved("63addf4343c5922bf71329bf", orderValid)
            )
        }
    }
}
