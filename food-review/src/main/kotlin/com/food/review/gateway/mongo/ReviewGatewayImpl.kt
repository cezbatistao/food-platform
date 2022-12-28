package com.food.review.gateway.mongo

import com.food.review.domain.Review
import com.food.review.gateway.ReviewGateway
import com.food.review.gateway.mongo.entity.ReviewMongo
import com.food.review.gateway.mongo.mapper.ReviewMongoMapper
import com.food.review.gateway.mongo.repository.ReviewRepository
import org.springframework.stereotype.Component

@Component
class ReviewGatewayImpl(private val reviewRepository: ReviewRepository): ReviewGateway {

    override fun save(review: Review): Review {
        val reviewMongo = ReviewMongoMapper.INSTANCE.map(review)
        val reviewMongoSaved = this.reviewRepository.save(reviewMongo)
        return ReviewMongoMapper.INSTANCE.map(reviewMongoSaved)
    }
}
