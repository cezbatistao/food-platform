package com.food.review.gateway

import com.fasterxml.jackson.core.type.TypeReference
import com.food.review.domain.Review
import com.food.review.gateway.kafka.ReviewSendGatewayImpl
import com.food.review.gateway.kafka.resource.DataResource
import com.food.review.gateway.kafka.resource.producer.ReviewEvent
import com.food.review.gateway.mongo.entity.OrderItemMongo
import com.food.review.lang.KafkaComponentTest
import java.time.LocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.instancio.Instancio
import org.instancio.Select
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Import

@Import(ReviewSendGatewayImpl::class)
class ReviewSendGatewayComponentTest: KafkaComponentTest() {

    @Autowired
    lateinit var reviewSendGateway: ReviewSendGateway

    @Value("\${spring.kafka.topic.review-created-event:review-created-event}")
    lateinit var topicCreatedEvent: String

    @Value("\${spring.kafka.topic.review-verified-event:review-verified-event}")
    lateinit var topicVerifiedEvent: String

    val modelReview = Instancio.of(Review::class.java)
        .set(Select.field("id"), "63aed5e39972ea2542808a3a")
        .generate(Select.field("items")) { gen -> gen.collection<OrderItemMongo>().minSize(1).maxSize(3) }
        .set(Select.field("createdAt"), LocalDateTime.now())
        .set(Select.field("updatedAt"), LocalDateTime.now())
        .toModel()

    @Test
    fun WhenSendCreated_thenProducerAMessage() {
        //given
        val review = Instancio.of(modelReview)
            .set(Select.field("text"), "")
            .create()

        //when
        this.reviewSendGateway.sendCreated(review)

        //then
        val record = getMessageFromTopic(this.topicCreatedEvent)

        assertThat(record!!.key()).isEqualTo(review.orderUuid.toString())
        val dataResource: DataResource<ReviewEvent> = objectMapper.readValue(
            record.value(), object : TypeReference<DataResource<ReviewEvent>>() {})

        assertThat(dataResource).isNotNull
        assertThat(dataResource.data).isNotNull

        val reviewCreatedEvent = dataResource.data
        assertThat(reviewCreatedEvent.uuid).isEqualTo(review.uuid.toString())
        assertThat(reviewCreatedEvent.userUuid).isEqualTo(review.userUuid.toString())
        assertThat(reviewCreatedEvent.userUuid).isEqualTo(review.userUuid.toString())
        assertThat(reviewCreatedEvent.restaurantUuid).isEqualTo(review.restaurant.uuid.toString())
        assertThat(reviewCreatedEvent.text).isEqualTo(review.text)
    }

    @Test
    fun WhenSendVerified_thenProducerAMessage() {
        //given
        val review = Instancio.of(modelReview)
            .create()

        //when
        this.reviewSendGateway.sendVerified(review)

        //then
        val record = getMessageFromTopic(this.topicVerifiedEvent)

        assertThat(record!!.key()).isEqualTo(review.orderUuid.toString())
        val dataResource: DataResource<ReviewEvent> = objectMapper.readValue(
            record.value(), object : TypeReference<DataResource<ReviewEvent>>() {})

        assertThat(dataResource).isNotNull
        assertThat(dataResource.data).isNotNull

        val reviewCreatedEvent = dataResource.data
        assertThat(reviewCreatedEvent.uuid).isEqualTo(review.uuid.toString())
        assertThat(reviewCreatedEvent.userUuid).isEqualTo(review.userUuid.toString())
        assertThat(reviewCreatedEvent.userUuid).isEqualTo(review.userUuid.toString())
        assertThat(reviewCreatedEvent.restaurantUuid).isEqualTo(review.restaurant.uuid.toString())
        assertThat(reviewCreatedEvent.text).isEqualTo(review.text)
    }
}
