package com.food.review.usecase;

import com.food.review.domain.error.FieldError;
import com.food.review.domain.Order;
import com.food.review.domain.Review;
import com.food.review.gateway.OrderGateway;
import com.food.review.gateway.ReviewGateway;
import com.food.review.usecase.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CreateReview {
    
    private OrderGateway orderGateway;
    private ReviewGateway reviewGateway;
    private Validator validator;

    @Autowired
    public CreateReview(OrderGateway orderGateway, ReviewGateway reviewGateway, Validator validator) {
        this.orderGateway = orderGateway;
        this.reviewGateway = reviewGateway;
        this.validator = validator;
    }

    public Review execute(Review review) {
        Set<ConstraintViolation<Review>> violations = validator.validate(review);
        if (!violations.isEmpty()) {
            List<FieldError> errors = this.create(violations);

            log.info("Validation error. Reason: {}", errors);

            throw new ValidationException("error.validationFields", errors);
        }

        Order order = this.orderGateway.getById(review.getOrderId());
        return this.reviewGateway.create(review);
    }

    private List<FieldError> create(final Set<ConstraintViolation<Review>> violations) {
        return violations.stream()
                .map(constraintViolation ->
                        FieldError.builder()
                                .message(constraintViolation.getMessage())
                                .messageTemplate(constraintViolation.getMessageTemplate()
                                        .replace("{", "")
                                        .replace("}", ""))
                                .field(constraintViolation.getPropertyPath().toString())
                                .invalidValue(constraintViolation.getInvalidValue())
                                .build())
                .collect(Collectors.toList());
    }
}
