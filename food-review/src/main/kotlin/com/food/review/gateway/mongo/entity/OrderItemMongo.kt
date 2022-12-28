package com.food.review.gateway.mongo.entity

import java.math.BigDecimal

data class OrderItemMongo(
    var uuid: String?,
    var menuItemUuid: String?,
    var name: String?,
    var amount: Int?,
    var unitValue: BigDecimal?)
