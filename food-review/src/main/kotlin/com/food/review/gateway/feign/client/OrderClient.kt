package com.food.review.gateway.feign.client

import com.food.review.gateway.feign.client.json.DataResponse
import com.food.review.gateway.feign.client.json.OrderResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(value = "orderApp", url = "\${feign.client.config.orderApp.baseUrl}")
interface OrderClient {

    @GetMapping(value = ["/api/v1/{userUuid}/orders/{uuid}"], consumes = [APPLICATION_JSON_VALUE])
    fun getByUserUuidAndUuid(@PathVariable("userUuid") userUuid: String,
                             @PathVariable("uuid") uuid: String): DataResponse<OrderResponse>

}
