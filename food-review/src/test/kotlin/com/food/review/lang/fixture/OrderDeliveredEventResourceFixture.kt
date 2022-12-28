package com.food.review.lang.fixture

import com.food.review.entrypoint.listener.resource.OrderDeliveredEventResource
import com.food.review.entrypoint.listener.resource.OrderItemResource
import java.time.LocalDateTime

class OrderDeliveredEventResourceFixture {

    companion object {
        fun getValid(): OrderDeliveredEventResource {
            return OrderDeliveredEventResource(
                "d0eff5fd-d26d-440d-9969-b20549d5f1f2",
                "dbb9c2bd-abde-48a3-891a-6229fc9b7c21",
                RestaurantResourceFixture.getValid(),
                arrayListOf(
                    OrderItemResource(
                        "843bfe62-9543-11eb-a8b3-0242ac130003",
                        "f4f08147-4d1d-4067-92fe-32e5ef411151",
                        "Pepperoni",
                        1,
                        "33.99"
                    ),
                    OrderItemResource(
                        "88e3812e-9543-11eb-a8b3-0242ac130003",
                        "9243b2c5-9050-4e81-b559-36cda7595a78",
                        "Mussarela",
                        2,
                        "31.99"
                    )
                ),
                "97.97",
                LocalDateTime.now(),
                LocalDateTime.now()
            )
        }
    }
}
