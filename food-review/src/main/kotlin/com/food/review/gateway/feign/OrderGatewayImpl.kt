package com.food.review.gateway.feign

import com.food.review.domain.Order
import com.food.review.gateway.OrderGateway
import com.food.review.gateway.feign.client.OrderClient
import com.food.review.gateway.feign.mapper.OrderResponseMapper
import java.util.UUID
import org.springframework.stereotype.Component

@Component
class OrderGatewayImpl(private val orderClient: OrderClient): OrderGateway {

    override fun findByUserUuidAndUuid(userUuid: UUID, uuid: UUID): Order {
        val dataResponse = orderClient.getByUserUuidAndUuid(userUuid.toString(), uuid.toString())
        return OrderResponseMapper.INSTANCE.map(dataResponse.data)
    }
}
