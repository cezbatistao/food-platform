package com.food.review.gateway.feign.client

import com.food.review.gateway.feign.client.json.DataResponse
import com.food.review.gateway.feign.client.json.OrderResponse

class OrderFallback: OrderClient {

    override fun getByUserUuidAndUuid(userUuid: String, uuid: String): DataResponse<OrderResponse> {
        TODO("Not yet implemented")
    }
}
