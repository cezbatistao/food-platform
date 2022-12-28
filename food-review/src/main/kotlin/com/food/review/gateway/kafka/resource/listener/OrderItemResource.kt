package com.food.review.gateway.kafka.resource.listener

import com.fasterxml.jackson.annotation.JsonProperty

class OrderItemResource(
    var uuid: String?,
    @JsonProperty("menu_item_uuid") var menuItemUuid: String?,
    var name: String?,
    var amount: Int?,
    @JsonProperty("unit_value") var unitValue: String?
)
