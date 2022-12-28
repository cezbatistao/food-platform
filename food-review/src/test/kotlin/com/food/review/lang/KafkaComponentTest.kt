package com.food.review.lang

import com.fasterxml.jackson.databind.ObjectMapper
import com.food.review.config.kafka.KafkaConfiguration
import java.time.Duration
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestComponent
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.IfProfileValue
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName

@ExtendWith(SpringExtension::class)
@DirtiesContext
@TestComponent
@SpringBootTest(classes = [KafkaConfiguration::class, KafkaAutoConfiguration::class, JacksonAutoConfiguration::class])
@ActiveProfiles("component-test")
@IfProfileValue(name = "spring.profiles.active", value = "component-test")
abstract class KafkaComponentTest {

    companion object {
        lateinit var kafkaContainer: KafkaContainer

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            kafkaContainer = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.3.0"))
            kafkaContainer.start()
        }

        @JvmStatic
        @DynamicPropertySource
        internal fun kafkaProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.kafka.bootstrap-servers") { kafkaContainer.bootstrapServers }
            registry.add("spring.kafka.consumer.key-deserializer") { "org.apache.kafka.common.serialization.StringDeserializer" }
            registry.add("spring.kafka.consumer.value-deserializer") { "org.apache.kafka.common.serialization.StringDeserializer" }
            registry.add("spring.kafka.producer.key-serializer") { "org.apache.kafka.common.serialization.StringSerializer" }
            registry.add("spring.kafka.producer.value-serializer") { "org.apache.kafka.common.serialization.StringSerializer" }
        }
    }

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    protected lateinit var kafkaTemplate: KafkaTemplate<String, String>

    @Autowired
    lateinit var kafkaProperties: KafkaProperties

    protected lateinit var consumer: KafkaConsumer<String, String>

    @BeforeEach
    fun setupEach() {
        val configs: MutableMap<String, Any> = kafkaProperties.consumer.buildProperties()
        configs[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaProperties.bootstrapServers
        consumer = KafkaConsumer<String, String>(configs)
    }

    protected fun getMessageFromTopic(topic: String): ConsumerRecord<String, String>? {
        consumer.subscribe(listOf(topic))
        val records = consumer.poll(Duration.ofSeconds(10))
        return records.records(topic).iterator().next()
    }

    @AfterEach
    protected fun closeConsumer() {
        consumer.close()
    }
}
