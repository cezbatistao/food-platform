package com.food.review.gateway

import com.food.review.domain.Order
import java.util.UUID

interface OrderGateway {

    fun findByUserUuidAndUuid(userUuid: UUID, uuid: UUID): Order

}
