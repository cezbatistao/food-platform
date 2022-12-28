package com.food.review.entrypoint.listener

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.food.review.config.log.LogKey.DATA
import com.food.review.config.log.LogKey.TOPIC
import com.food.review.entrypoint.listener.mapper.OrderDeliveredEventResourceMapper
import com.food.review.entrypoint.listener.resource.DataResource
import com.food.review.entrypoint.listener.resource.OrderDeliveredEventResource
import com.food.review.usecase.CreateReview
import java.util.concurrent.CountDownLatch
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.kv
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class OrderDeliveredEventListener(
    private val createReview: CreateReview,
    private val objectMapper: ObjectMapper,
    private var latch: CountDownLatch = CountDownLatch(1)
) {

    private val logger = KotlinLogging.logger {}

    @KafkaListener(topics = arrayOf("\${spring.kafka.topic.order-delivered-event:order-delivered-event}"))
    fun listen(@Payload message: String,
               @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
               @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) key: String,
               @Header(KafkaHeaders.RECEIVED_PARTITION_ID) partitionId: String,
               @Header(KafkaHeaders.OFFSET) offset: String) {
        try {
            val dataResource: DataResource<OrderDeliveredEventResource> = objectMapper.readValue(
                message, object : TypeReference<DataResource<OrderDeliveredEventResource>>() {})
            logger.info(
                "Message received on {}",
                kv(TOPIC.toString(), topic),
                kv(DATA.toString(), dataResource.data))

            val order = dataResource.data.let { OrderDeliveredEventResourceMapper.INSTANCE.map(it) }
            createReview.execute(order)
        } catch (ex: Exception) {
            logger.error("Error message from {} at {} with {}",
                topic,
                partitionId,
                key,
                message,
                ex);
        }
        latch.countDown()
    }

    fun getLatch(): CountDownLatch? {
        return latch
    }

    fun reset() {
        latch = CountDownLatch(1)
    }
}