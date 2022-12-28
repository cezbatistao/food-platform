package com.food.review.entrypoint.listener.resource

import com.fasterxml.jackson.annotation.JsonProperty

class OrderItemResource(
    var uuid: String?,
    @JsonProperty("menu_item_uuid") var menuItemUuid: String?,
    var name: String?,
    var amount: Int?,
    @JsonProperty("unit_value") var unitValue: String?
)
