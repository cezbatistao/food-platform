package com.food.review.lang.customizer

import com.food.review.domain.Order
import com.food.review.domain.OrderItem
import com.food.review.domain.Restaurant
import com.navercorp.fixturemonkey.customizer.ArbitraryCustomizer

class OrderArbitraryCustomizer {

    static ArbitraryCustomizer<Order> orderFixture = new ArbitraryCustomizer<Order>() {
        @Override
        Order customizeFixture(Order order) {
            return order.toBuilder()
                    .id(UUID.fromString("d0eff5fd-d26d-440d-9969-b20549d5f1f2"))
                    .restaurant(Restaurant.builder()
                            .id(UUID.fromString("36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c"))
                            .name("Domino's Pizza")
                            .build())
                    .orderItem(OrderItem.builder()
                            .id(UUID.fromString("843bfe62-9543-11eb-a8b3-0242ac130003"))
                            .name("Pepperoni")
                            .amount(1)
                            .unitValue(new BigDecimal("33.99"))
                            .build())
                    .orderItem(OrderItem.builder()
                            .id(UUID.fromString("88e3812e-9543-11eb-a8b3-0242ac130003"))
                            .name("Mussarela")
                            .amount(2)
                            .unitValue(new BigDecimal("31.99"))
                            .build())
                    .total(new BigDecimal("97.97"))
                    .build()
        }
    }
}
