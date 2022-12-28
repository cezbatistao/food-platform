package com.food.review.lang

import com.food.review.config.mongo.MongoConfiguration
import com.food.review.lang.config.MongoDBTestConfiguration
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.MongoDBContainer

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = arrayOf(MongoDBTestConfiguration::class, MongoConfiguration::class))
@ActiveProfiles("component-test")
class MongoRepositoryComponentTest {

    companion object {
        lateinit var mongoDBContainer: MongoDBContainer

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            mongoDBContainer = MongoDBContainer("mongo:5.0.14")
            mongoDBContainer.start()

            System.setProperty("spring.data.mongodb.ct.uri.container", mongoDBContainer.getReplicaSetUrl("review_ct_db"))
        }

        @AfterAll
        @JvmStatic
        internal fun afterAll() {
            mongoDBContainer.stop()
        }
    }
}
