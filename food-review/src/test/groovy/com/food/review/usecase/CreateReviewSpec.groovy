package com.food.review.usecase

import com.food.review.domain.Order
import com.food.review.domain.Review
import com.food.review.domain.error.FieldError
import com.food.review.gateway.OrderGateway
import com.food.review.gateway.ReviewGateway
import com.food.review.lang.UnitSpec
import com.food.review.usecase.exception.ValidationException
import spock.lang.Unroll

import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory
import java.time.LocalDateTime

import static com.food.review.lang.customizer.FieldErrorArbitraryCustomizer.*
import static com.food.review.lang.customizer.ReviewArbitraryCustomizer.reviewSaved
import static com.food.review.lang.customizer.ReviewArbitraryCustomizer.reviewToSave

class CreateReviewSpec extends UnitSpec {

    CreateReview createReview

    OrderGateway orderGateway
    ReviewGateway reviewGateway

    def setup() {
        orderGateway = Mock(OrderGateway)
        reviewGateway = Mock(ReviewGateway)
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        createReview = new CreateReview(orderGateway, reviewGateway, validator)
    }

    @Unroll("and size of fields error with #totalErrors error(s)")
    def "should be a validation exception with fields errors from review"() {
        given:
        Review reviewToSave = Review.builder()
                .orderId(orderId)
                .username(username)
                .name(name)
                .text(text)
                .createdAt(createdAt)
                .build()

        def fieldErrorsFixture = fieldErrors.collect {
            this.fixture.giveMeOne(FieldError.class, it)
        }

        0 * orderGateway.getById(_)
        0 * reviewGateway.create(_)

        when:
        createReview.execute(reviewToSave)

        then:
        def error = thrown(ValidationException)
        error.message == "error.validationFields"
        error.fieldErrors.size() == totalErrors
        error.fieldErrors.every { fieldErrorsFixture.contains(it) }

        where:
        orderId           | username | name             | text    | createdAt                       | totalErrors | fieldErrors
        null              | null     | null             | null    | null                            | 5           | [fieldErrorFromUsername, fieldErrorFromOrderIdNotNull, fieldErrorFromNameNotBlank, fieldErrorFromTextNotBlank, fieldErrorFromCreatedAtNotNull]
        UUID.randomUUID() | "cezb"   | "Carlos Eduardo" | "Texto" | LocalDateTime.now().plusDays(1) | 2           | [fieldErrorFromTextMin, fieldErrorFromCreatedAtPastOrPresent]
    }

    def "should be a valid and saved review"() {
        given:
        Review reviewToSaveFixture = this.fixture.giveMeOne(Review.class, reviewToSave)
        Review reviewSavedFixture = this.fixture.giveMeOne(Review.class, reviewSaved)

        1 * orderGateway.getById(reviewToSaveFixture.getOrderId()) >> new Order()
        1 * reviewGateway.create(reviewToSaveFixture) >> reviewSavedFixture

        when:
        def savedReview = createReview.execute(reviewToSaveFixture)

        then:
        savedReview
        savedReview.id
        savedReview.orderId == reviewToSaveFixture.orderId
        savedReview.username == reviewToSaveFixture.username
        savedReview.name == reviewToSaveFixture.name
        savedReview.text == reviewToSaveFixture.text
        savedReview.createdAt == reviewToSaveFixture.createdAt
    }
}
