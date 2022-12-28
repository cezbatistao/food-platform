package com.food.review.entrypoint.listener.mapper

import com.food.review.domain.Order
import com.food.review.gateway.kafka.resource.listener.OrderDeliveredEventResource
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface OrderDeliveredEventResourceMapper {

    companion object {
        var INSTANCE = Mappers.getMapper(OrderDeliveredEventResourceMapper::class.java)
    }

    fun map(orderDeliveredEventResource: OrderDeliveredEventResource): Order

}
