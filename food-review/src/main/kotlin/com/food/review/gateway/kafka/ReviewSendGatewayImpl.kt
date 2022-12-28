package com.food.review.gateway.kafka

import com.food.review.config.log.LogKey.REVIEW
import com.food.review.config.log.LogKey.TOPIC
import com.food.review.domain.Review
import com.food.review.gateway.ReviewSendGateway
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.kv
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ReviewSendGatewayImpl(
    @Value("\${spring.kafka.topic.review-created-event:review-created-event}") val topic: String
): ReviewSendGateway {

    private val logger = KotlinLogging.logger {}

    override fun sendCreated(review: Review) {
        logger.info("Send message to {}",
            kv(TOPIC.toString(), this.topic),
            kv(REVIEW.toString(), review))
    }
}
