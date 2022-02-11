package com.food.review.gateway.mongo;

import com.food.review.domain.Review;
import com.food.review.gateway.ReviewGateway;
import com.food.review.gateway.mongo.entity.ReviewMongo;
import com.food.review.gateway.mongo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ReviewGatewayImpl implements ReviewGateway {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewGatewayImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review create(Review review) {
        ReviewMongo reviewMongo = ReviewMongo.builder()
                .id(UUID.randomUUID())
                .orderId(review.getOrderId())
                .username(review.getUsername())
                .name(review.getName())
                .text(review.getText())
                .createdAt(review.getCreatedAt())
                .build();

        ReviewMongo savedReviewMongo = this.reviewRepository.save(reviewMongo);

        return review.toBuilder()
                .id(savedReviewMongo.getId())
                .build();
    }
}
