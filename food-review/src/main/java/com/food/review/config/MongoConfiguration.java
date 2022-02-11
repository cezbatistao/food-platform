package com.food.review.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories("com.food.review.gateway.mongo")
public class MongoConfiguration {
}
