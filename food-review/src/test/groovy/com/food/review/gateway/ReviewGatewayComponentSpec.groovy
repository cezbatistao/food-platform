package com.food.review.gateway

import com.food.review.domain.Review
import com.food.review.gateway.mongo.repository.ReviewRepository
import com.food.review.lang.MongoRepositoryComponentSpec
import org.springframework.beans.factory.annotation.Autowired

import static com.food.review.lang.customizer.ReviewArbitraryCustomizer.getReviewToSaveFixture

class ReviewGatewayComponentSpec extends MongoRepositoryComponentSpec {

    @Autowired
    ReviewGateway reviewGateway

    @Autowired
    ReviewRepository reviewRepository

    def "should be create a review from order"() {
        given:
        Review reviewToSaveFixture = this.fixture.giveMeOne(Review.class, reviewToSaveFixture)

        when:
        def reviewCreated= reviewGateway.create(reviewToSaveFixture)

        then:
        def reviewFoundByIdOptional = reviewRepository.findById(reviewCreated.id)
        reviewFoundByIdOptional.isPresent()

        and:
        def reviewFoundById = reviewFoundByIdOptional.get()
        reviewFoundById.id
        reviewFoundById.orderId == reviewToSaveFixture.orderId
        reviewFoundById.username == reviewToSaveFixture.username
        reviewFoundById.name == reviewToSaveFixture.name
        reviewFoundById.text == reviewToSaveFixture.text
        reviewFoundById.createdAt == reviewToSaveFixture.createdAt
    }
}
