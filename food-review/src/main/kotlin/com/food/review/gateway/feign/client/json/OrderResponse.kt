package com.food.review.gateway.feign.client.json

import com.fasterxml.jackson.annotation.JsonProperty

data class OrderResponse(val uuid: String,
                         @JsonProperty("user_uuid") val userUuid: String,
                         val restaurant: RestaurantResponse,
                         val items: List<OrderItemResponse>)
