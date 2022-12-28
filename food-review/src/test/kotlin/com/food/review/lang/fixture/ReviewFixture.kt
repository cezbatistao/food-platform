package com.food.review.lang.fixture

import com.food.review.domain.Review
import com.food.review.domain.ReviewStatus.CREATED
import java.time.LocalDateTime
import java.util.UUID

class ReviewFixture {

    companion object {
        fun getValidCreated(): Review {
            return Review(
                CREATED, OrderFixture.getValid()
            )
        }

        fun getValidCreatedSaved(): Review {
            return Review(
                "63aae35dcf1dd00171077748",
                UUID.fromString("f58ceb5b-6e70-489c-907b-0efe3b2feca5"),
                CREATED,
                OrderFixture.getValid(),
                "",
                LocalDateTime.of(2022, 12, 7, 20, 21, 45),
                LocalDateTime.of(2022, 12, 7, 20, 21, 46)
            )
        }
    }
}
