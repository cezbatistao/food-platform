package com.food.review.lang.config

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@SpringBootApplication
@ComponentScan(basePackages = arrayOf("com.food.review.gateway.mongo"))
class MongoDBTestConfiguration {
}