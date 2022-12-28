package com.food.review.lang.fixture

import com.food.review.domain.Order
import com.food.review.domain.OrderItem
import java.util.UUID

class OrderFixture {

    companion object {
        fun getValid(): Order {
            return Order(
                UUID.fromString("d0eff5fd-d26d-440d-9969-b20549d5f1f2"),
                UUID.fromString("dbb9c2bd-abde-48a3-891a-6229fc9b7c21"),
                RestaurantFixture.getValid(),
                arrayListOf(
                    OrderItem(
                        UUID.fromString("843bfe62-9543-11eb-a8b3-0242ac130003"),
                        UUID.fromString("f4f08147-4d1d-4067-92fe-32e5ef411151"),
                        "Pepperoni"
                    ),
                    OrderItem(
                        UUID.fromString("88e3812e-9543-11eb-a8b3-0242ac130003"),
                        UUID.fromString("9243b2c5-9050-4e81-b559-36cda7595a78"),
                        "Mussarela"
                    )
                )
            )
        }
    }
}
