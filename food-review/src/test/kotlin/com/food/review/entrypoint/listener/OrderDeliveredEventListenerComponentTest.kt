package com.food.review.entrypoint.listener

import com.food.review.domain.Order
import com.food.review.domain.Review
import com.food.review.entrypoint.listener.resource.DataResource
import com.food.review.lang.KafkaComponentTest
import com.food.review.lang.fixture.OrderDeliveredEventResourceFixture
import com.food.review.lang.fixture.OrderFixture
import com.food.review.usecase.CreateReview
import com.ninjasquad.springmockk.MockkBean
import io.mockk.justRun
import io.mockk.slot
import io.mockk.verify
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

class OrderDeliveredEventListenerComponentTest: KafkaComponentTest() {

    @Value("\${spring.kafka.topic.order-delivered-event:order-delivered-event}")
    private lateinit var topic: String

    @Autowired
    private lateinit var orderDeliveredEventListener: OrderDeliveredEventListener

    @MockkBean
    private lateinit var createReview: CreateReview

    @Test
    fun WhenReceiveMessage_thenConverterAtOrderToCreateReview() {
        val orderSlot = slot<Order>()
        justRun { createReview.execute(capture(orderSlot)) }

        val orderDeliveredEventResource = OrderDeliveredEventResourceFixture.getValid()
        val message = objectMapper.writeValueAsString(DataResource(orderDeliveredEventResource))

        kafkaTemplate.send(topic, orderDeliveredEventResource.uuid!!, message)
        this.orderDeliveredEventListener.getLatch()!!.await()

        assertThat(this.orderDeliveredEventListener.getLatch()!!.count).isZero

        verify(exactly = 1) { createReview.execute(any()) }

        val orderCaptured = orderSlot.captured
        assertThat(orderCaptured.uuid.toString()).isEqualTo(orderDeliveredEventResource.uuid)
        assertThat(orderCaptured.userUuid.toString()).isEqualTo(orderDeliveredEventResource.userUuid)
        assertThat(orderCaptured.restaurant.uuid.toString()).isEqualTo(orderDeliveredEventResource.restaurant!!.uuid)
        assertThat(orderCaptured.restaurant.name).isEqualTo(orderDeliveredEventResource.restaurant!!.name)
        assertThat(orderCaptured.total).isEqualTo(orderDeliveredEventResource.total)

        //and
        assertThat(orderCaptured.items).hasSize(orderDeliveredEventResource.items!!.size)
        orderCaptured.items.forEach { orderItemCaptured ->
            val orderItemResource =
                orderDeliveredEventResource.items!!.find { it.uuid == orderItemCaptured.uuid.toString() }
            assertThat(orderItemResource).isNotNull

            assertThat(orderItemCaptured.menuItemUuid.toString()).isEqualTo(orderItemResource!!.menuItemUuid)
            assertThat(orderItemCaptured.name).isEqualTo(orderItemResource.name)
            assertThat(orderItemCaptured.amount).isEqualTo(orderItemResource.amount)
            assertThat(orderItemCaptured.unitValue).isEqualTo(orderItemResource.unitValue)
        }
    }
}
