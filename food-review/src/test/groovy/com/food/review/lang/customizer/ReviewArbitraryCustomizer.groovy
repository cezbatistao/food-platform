package com.food.review.lang.customizer

import com.food.review.domain.Review
import com.navercorp.fixturemonkey.customizer.ArbitraryCustomizer

import java.time.LocalDateTime

class ReviewArbitraryCustomizer {

    private static Review.ReviewBuilder DEFAULT_REVIEW = Review.builder()
            .orderId(UUID.fromString("d0eff5fd-d26d-440d-9969-b20549d5f1f2"))
            .username("ibrahimtpaula")
            .name("Ibrahim Taborda Paula")
            .text("Lorem ipsum venenatis aenean per integer nibh vehicula purus leo massa, aliquet ut " +
                    "cursus auctor imperdiet mauris convallis vel nunc.")
            .createdAt(LocalDateTime.of(2022, 2, 7, 20, 21, 45));

    static ArbitraryCustomizer<Review> reviewToSaveFixture = new ArbitraryCustomizer<Review>() {
        @Override
        Review customizeFixture(Review review) {
            return DEFAULT_REVIEW
                    .build()
        }
    }

    static ArbitraryCustomizer<Review> reviewSavedFixture = new ArbitraryCustomizer<Review>() {
        @Override
        Review customizeFixture(Review review) {
            return DEFAULT_REVIEW
                    .id(UUID.randomUUID())
                    .build()
        }
    }
}
