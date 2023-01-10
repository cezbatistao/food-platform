package com.food.review.gateway.feign.mapper

import com.food.review.domain.Order
import com.food.review.gateway.feign.client.json.OrderResponse
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface OrderResponseMapper {

    companion object {
        var INSTANCE = Mappers.getMapper(OrderResponseMapper::class.java)
    }

    fun map(orderResponse: OrderResponse): Order

}