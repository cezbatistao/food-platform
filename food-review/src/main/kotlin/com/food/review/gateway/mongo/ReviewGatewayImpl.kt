package com.food.review.gateway.mongo

import com.food.restaurant.domain.exception.EntityNotFoundException
import com.food.review.domain.Review
import com.food.review.domain.ReviewPageable
import com.food.review.gateway.ReviewGateway
import com.food.review.gateway.mongo.entity.RestaurantMongo
import com.food.review.gateway.mongo.entity.ReviewMongo
import com.food.review.gateway.mongo.mapper.ReviewMongoMapper
import com.food.review.gateway.mongo.repository.ReviewRepository
import java.util.UUID
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.stereotype.Component

@Component
class ReviewGatewayImpl(private val reviewRepository: ReviewRepository): ReviewGateway {

    override fun save(review: Review): Review {
        val reviewMongo = ReviewMongoMapper.INSTANCE.map(review)
        val reviewMongoSaved = this.reviewRepository.save(reviewMongo)
        return ReviewMongoMapper.INSTANCE.map(reviewMongoSaved)
    }

    override fun findByUserUuid(reviewPageable: ReviewPageable): Page<Review> {
        val pageable = PageRequest.of(reviewPageable.page, reviewPageable.size, DESC, "createdAt")

        val reviewMongoExample = ReviewMongo()
        reviewMongoExample.userUuid = reviewPageable.userUuid.toString()
        reviewPageable.restaurantUuid?.let {
            reviewMongoExample.restaurant = RestaurantMongo(reviewPageable.restaurantUuid.toString(), null)
        }
        reviewPageable.status?.let {
            reviewMongoExample.status = reviewPageable.status
        }

        val example: Example<ReviewMongo> = Example.of(reviewMongoExample)

        val reviewsMongoPageable = this.reviewRepository.findAll(example, pageable)
        return reviewsMongoPageable.map { ReviewMongoMapper.INSTANCE.map(it) }
    }

    override fun findByUserUuidAndUuid(userUuid: UUID, uuid: UUID): Review {
        return this.reviewRepository.findByUserUuidAndUuid(userUuid.toString(), uuid.toString())
            .map { ReviewMongoMapper.INSTANCE.map(it) }
            .orElseThrow { EntityNotFoundException("0001", "entityNotFoundException", "Review doesn't exists") }
    }
}
