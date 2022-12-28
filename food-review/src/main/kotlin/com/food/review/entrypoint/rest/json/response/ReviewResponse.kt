package com.food.review.entrypoint.rest.json.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.food.review.domain.ReviewStatus
import java.time.LocalDateTime

class ReviewResponse(
    var uuid: String?,
    var status: ReviewStatus?,
    var restaurant: RestaurantResponse?,
    @get:JsonProperty("order_uuid") var orderUuid: String?,
    @get:JsonProperty("user_uuid") var userUuid: String?,
    var text: String?,
    @get:JsonProperty("created_at") var createdAt: LocalDateTime?,
    @get:JsonProperty("updated_at") var updatedAt: LocalDateTime?
)
