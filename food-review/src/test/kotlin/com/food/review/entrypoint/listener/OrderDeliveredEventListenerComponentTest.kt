package com.food.review.entrypoint.listener

import com.food.review.domain.Order
import com.food.review.entrypoint.listener.resource.FakeResource
import com.food.review.gateway.kafka.resource.DataResource
import com.food.review.lang.KafkaComponentTest
import com.food.review.lang.fixture.OrderDeliveredEventResourceFixture
import com.food.review.usecase.CreateReview
import com.ninjasquad.springmockk.MockkBean
import io.mockk.justRun
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Import

@Import(OrderDeliveredEventListener::class)
class OrderDeliveredEventListenerComponentTest: KafkaComponentTest() {

    @Value("\${spring.kafka.topic.order-delivered-event:order-delivered-event}")
    private lateinit var topic: String

    @Autowired
    private lateinit var orderDeliveredEventListener: OrderDeliveredEventListener

    @MockkBean
    private lateinit var createReview: CreateReview

    @BeforeEach
    fun setupTest() {
        this.orderDeliveredEventListener.reset()
    }

    @Test
    fun WhenReceiveMessage_thenConverterAtOrderToCreateReview() {
        //given
        val orderSlot = slot<Order>()
        justRun { createReview.execute(capture(orderSlot)) }

        val orderDeliveredEventResource = OrderDeliveredEventResourceFixture.getValid()
        val message = objectMapper.writeValueAsString(DataResource(orderDeliveredEventResource))

        kafkaTemplate.send(topic, orderDeliveredEventResource.uuid!!, message)
        this.orderDeliveredEventListener.getLatch()!!.await()

        //when
        assertThat(this.orderDeliveredEventListener.getLatch()!!.count).isZero

        //then
        verify(exactly = 1) { createReview.execute(any()) }

        val orderCaptured = orderSlot.captured
        assertThat(orderCaptured.uuid.toString()).isEqualTo(orderDeliveredEventResource.uuid)
        assertThat(orderCaptured.userUuid.toString()).isEqualTo(orderDeliveredEventResource.userUuid)
        assertThat(orderCaptured.restaurant.uuid.toString()).isEqualTo(orderDeliveredEventResource.restaurant!!.uuid)
        assertThat(orderCaptured.restaurant.name).isEqualTo(orderDeliveredEventResource.restaurant!!.name)

        //and
        assertThat(orderCaptured.items).hasSize(orderDeliveredEventResource.items!!.size)
        orderCaptured.items.forEach { orderItemCaptured ->
            val orderItemResource =
                orderDeliveredEventResource.items!!.find { it.uuid == orderItemCaptured.uuid.toString() }
            assertThat(orderItemResource).isNotNull

            assertThat(orderItemCaptured.menuItemUuid.toString()).isEqualTo(orderItemResource!!.menuItemUuid)
            assertThat(orderItemCaptured.name).isEqualTo(orderItemResource.name)
        }
    }

    @Test
    fun WhenReceiveWrongMessage_thenDoNothing() {
        //given
        justRun { createReview.execute(any()) }

        val orderDeliveredEventResource = OrderDeliveredEventResourceFixture.getValid()
        val message = objectMapper.writeValueAsString(FakeResource())

        kafkaTemplate.send(topic, orderDeliveredEventResource.uuid!!, message)
        this.orderDeliveredEventListener.getLatch()!!.await()

        //when
        assertThat(this.orderDeliveredEventListener.getLatch()!!.count).isZero

        //then
        verify(exactly = 0) { createReview.execute(any()) }
    }
}
