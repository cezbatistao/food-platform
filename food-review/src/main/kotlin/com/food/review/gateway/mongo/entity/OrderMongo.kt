package com.food.review.gateway.mongo.entity

import java.math.BigDecimal

data class OrderMongo(
    var uuid: String?,
    var userUuid: String?,
    var restaurant: RestaurantMongo?,
    var items: List<OrderItemMongo>?,
    var total: BigDecimal?)
