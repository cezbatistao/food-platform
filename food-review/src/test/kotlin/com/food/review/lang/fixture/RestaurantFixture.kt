package com.food.review.lang.fixture

import com.food.review.domain.Restaurant
import java.util.UUID

class RestaurantFixture {

    companion object {
        fun getValid(): Restaurant {
            return Restaurant(
                UUID.fromString("36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c"),
                "Domino's Pizza"
            )
        }
    }
}