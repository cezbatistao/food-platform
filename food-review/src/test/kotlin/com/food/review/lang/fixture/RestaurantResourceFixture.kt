package com.food.review.lang.fixture

import com.food.review.entrypoint.listener.resource.RestaurantResource

class RestaurantResourceFixture {

    companion object {
        fun getValid(): RestaurantResource {
            return RestaurantResource(
                "36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c",
                "Domino's Pizza"
            )
        }
    }
}
