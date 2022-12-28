package com.food.review.gateway.mongo.entity

import com.food.review.domain.ReviewStatus
import java.time.LocalDateTime
import java.util.UUID
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document

@Document("reviews")
class ReviewMongo(
    @Id var id: String?,
    var uuid: String?,
    var status: ReviewStatus?,
    var restaurant: RestaurantMongo?,
    var orderUuid: String?,
    var userUuid: String?,
    var items: List<OrderItemMongo>?,
    var text: String?,
    @CreatedDate var createdAt: LocalDateTime?,
    @LastModifiedDate var updatedAt: LocalDateTime?) {

    constructor(): this(null, null, null, null, null, null, null, null, null, null)
}
