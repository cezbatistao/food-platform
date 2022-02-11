package com.food.review.lang

import com.food.review.config.MongoConfiguration
import com.food.review.lang.config.MongoDBTestConfiguration
import groovy.util.logging.Slf4j
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.containers.MongoDBContainer

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = [MongoDBTestConfiguration.class, MongoConfiguration.class])
@ActiveProfiles("component-test")
abstract class MongoRepositoryComponentSpec extends UnitSpec {

    static MongoDBContainer mongoDBContainer

    static {
        mongoDBContainer = new MongoDBContainer("mongo:4.0.10")
        mongoDBContainer.start()

        System.setProperty("spring.data.mongodb.ct.uri.container", mongoDBContainer.getReplicaSetUrl("review_ct_db"))
    }
}
