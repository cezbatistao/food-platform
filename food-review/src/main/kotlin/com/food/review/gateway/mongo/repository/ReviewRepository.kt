package com.food.review.gateway.mongo.repository

import com.food.review.gateway.mongo.entity.ReviewMongo
import org.springframework.data.mongodb.repository.MongoRepository

interface ReviewRepository: MongoRepository<ReviewMongo, String> {

}