package com.food.review.gateway.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.food.review.config.log.LogKey.REVIEW
import com.food.review.config.log.LogKey.TOPIC
import com.food.review.domain.Review
import com.food.review.entrypoint.rest.json.response.DataResponse
import com.food.review.gateway.ReviewSendGateway
import com.food.review.gateway.kafka.resource.producer.ReviewEvent
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.kv
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class ReviewSendGatewayImpl(
    val kafkaTemplate: KafkaTemplate<String, String>,
    val objectMapper: ObjectMapper,
    @Value("\${spring.kafka.topic.review-created-event:review-created-event}") val topicCreatedEvent: String,
    @Value("\${spring.kafka.topic.review-verified-event:review-verified-event}") val topicVeriFiedEvent: String
): ReviewSendGateway {

    private val logger = KotlinLogging.logger {}

    override fun sendCreated(review: Review) {
        this.send(this.topicCreatedEvent, review)
    }

    override fun sendVerified(review: Review) {
        this.send(this.topicVeriFiedEvent, review)
    }

    private fun send(topic: String, review: Review) {
        logger.info("Send message to {}",
            kv(TOPIC.toString(), topic),
            kv(REVIEW.toString(), review))

        val reviewEvent = ReviewEvent(review.uuid.toString(), review.userUuid.toString(),
            review.orderUuid.toString(), review.restaurant.uuid.toString(), review.text!!)

        val message = objectMapper.writeValueAsString(DataResponse(reviewEvent))
        this.kafkaTemplate.send(topic, review.orderUuid.toString(), message)
    }
}
