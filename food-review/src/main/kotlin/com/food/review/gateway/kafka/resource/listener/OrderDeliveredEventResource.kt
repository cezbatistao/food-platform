package com.food.review.gateway.kafka.resource.listener

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

class OrderDeliveredEventResource(
    val uuid: String?,
    @JsonProperty("user_uuid") val userUuid: String?,
    val restaurant: RestaurantResource?,
    val items: List<OrderItemResource>?,
    val total: String?,
    @JsonProperty("date_created") val dateCreated: LocalDateTime?,
    @JsonProperty("date_updated")  val dateUpdated: LocalDateTime?)
