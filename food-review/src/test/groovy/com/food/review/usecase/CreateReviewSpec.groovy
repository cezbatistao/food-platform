package com.food.review.usecase

import com.food.review.domain.Order
import com.food.review.domain.Review
import com.food.review.domain.error.FieldError
import com.food.review.gateway.OrderGateway
import com.food.review.gateway.ReviewGateway
import com.food.review.lang.UnitSpec
import com.food.review.lang.customizer.OrderArbitraryCustomizer
import com.food.review.usecase.exception.OrderNotFoundException
import com.food.review.usecase.exception.ValidationException
import spock.lang.Unroll

import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory
import java.time.LocalDateTime

import static com.food.review.lang.customizer.FieldErrorArbitraryCustomizer.getFieldErrorFromCreatedAtNotNull
import static com.food.review.lang.customizer.FieldErrorArbitraryCustomizer.getFieldErrorFromCreatedAtPastOrPresent
import static com.food.review.lang.customizer.FieldErrorArbitraryCustomizer.getFieldErrorFromNameNotBlank
import static com.food.review.lang.customizer.FieldErrorArbitraryCustomizer.getFieldErrorFromOrderIdNotNull
import static com.food.review.lang.customizer.FieldErrorArbitraryCustomizer.getFieldErrorFromTextMin
import static com.food.review.lang.customizer.FieldErrorArbitraryCustomizer.getFieldErrorFromTextNotBlank
import static com.food.review.lang.customizer.FieldErrorArbitraryCustomizer.getFieldErrorFromUsername
import static com.food.review.lang.customizer.OrderArbitraryCustomizer.orderFixture
import static com.food.review.lang.customizer.ReviewArbitraryCustomizer.reviewToSaveFixture
import static com.food.review.lang.customizer.ReviewArbitraryCustomizer.reviewSavedFixture

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
            def fieldError = this.fixture.giveMeOne(FieldError.class, it)
            if(fieldError.field == 'createdAt' && fieldError.messageTemplate.contains('PastOrPresent' )) { // I did this because of the seconds and milliseconds of createAt and invalidValue
                fieldError = fieldError.toBuilder().invalidValue(createdAt).build()
            }
            fieldError
        }

        0 * orderGateway.getById(_)
        0 * reviewGateway.create(_)

        when:
        createReview.execute(reviewToSave)

        then:
        def error = thrown(ValidationException)
        error.code == "error.validation_fields"
        error.message == "Error on validation fields"
        error.fieldErrors.size() == totalErrors
        error.fieldErrors.every { fieldErrorsFixture.contains(it) }

        where:
        orderId           | username | name             | text    | createdAt                       | totalErrors | fieldErrors
        null              | null     | null             | null    | null                            | 5           | [fieldErrorFromUsername, fieldErrorFromOrderIdNotNull, fieldErrorFromNameNotBlank, fieldErrorFromTextNotBlank, fieldErrorFromCreatedAtNotNull]
        UUID.randomUUID() | "cezb"   | "Carlos Eduardo" | "Texto" | LocalDateTime.now().plusDays(1) | 2           | [fieldErrorFromTextMin, fieldErrorFromCreatedAtPastOrPresent]
    }

    def "should be a order not found exception when order doesn't exists"() {
        given:
        Review reviewToSaveFixture = this.fixture.giveMeOne(Review.class, reviewToSaveFixture)

        1 * orderGateway.getById(reviewToSaveFixture.getOrderId()) >> {UUID uuid ->
            throw new OrderNotFoundException(reviewToSaveFixture.getOrderId(), "error.order_not_found", "Error on find order")
        }
        0 * reviewGateway.create(_)

        when:
        createReview.execute(reviewToSaveFixture)

        then:
        def error = thrown(OrderNotFoundException)
        error.orderId == reviewToSaveFixture.getOrderId()
        error.code == "error.order_not_found"
        error.message == "Error on find order"
    }

    def "should be a valid and saved review"() {
        given:
        Review reviewToSaveFixture = this.fixture.giveMeOne(Review.class, reviewToSaveFixture)
        Review reviewSavedFixture = this.fixture.giveMeOne(Review.class, reviewSavedFixture)
        Order orderFixture = this.fixture.giveMeOne(Order.class, orderFixture)

        1 * orderGateway.getById(reviewToSaveFixture.getOrderId()) >> orderFixture
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
