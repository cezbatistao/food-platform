package com.food.review.gateway.mongo.entity

import com.food.review.domain.ReviewStatus
import java.time.LocalDateTime
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document

@Document("reviews")
class ReviewMongo(
    @Id var id: String?,
    var uuid: String?,
    var status: ReviewStatus?,
    var order: OrderMongo?,
    var text: String?,
    @CreatedDate var createdAt: LocalDateTime?,
    @LastModifiedDate var updatedAt: LocalDateTime?)
