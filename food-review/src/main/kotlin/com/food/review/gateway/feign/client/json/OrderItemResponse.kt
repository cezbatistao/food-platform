package com.food.review.gateway.feign.client.json

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class OrderItemResponse(val uuid: UUID,
                             @JsonProperty("menu_item_uuid") val menuItemUuid: UUID,
                             val name: String)