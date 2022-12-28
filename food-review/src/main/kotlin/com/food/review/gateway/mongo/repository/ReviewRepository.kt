package com.food.review.gateway.mongo.repository

import com.food.review.gateway.mongo.entity.ReviewMongo
import java.util.Optional
import java.util.UUID
import org.springframework.data.mongodb.repository.MongoRepository

interface ReviewRepository: MongoRepository<ReviewMongo, String> {

    fun findByUserUuidAndUuid(userUuid: String, uuid: String): Optional<ReviewMongo>

}