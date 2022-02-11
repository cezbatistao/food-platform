package com.food.review.gateway.mongo.repository;

import com.food.review.gateway.mongo.entity.ReviewMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReviewRepository extends MongoRepository<ReviewMongo, UUID> {
}
