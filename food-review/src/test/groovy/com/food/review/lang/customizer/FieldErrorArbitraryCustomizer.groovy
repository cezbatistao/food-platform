package com.food.review.lang.customizer


import com.food.review.domain.error.FieldError
import com.navercorp.fixturemonkey.customizer.ArbitraryCustomizer

import java.time.LocalDateTime

class FieldErrorArbitraryCustomizer {

    static ArbitraryCustomizer<FieldError> fieldErrorFromUsername = new ArbitraryCustomizer<FieldError>() {
        @Override
        FieldError customizeFixture(FieldError fieldError) {
            return FieldError.builder()
                    .message("must not be blank")
                    .messageTemplate("javax.validation.constraints.NotBlank.message")
                    .field("username")
                    .invalidValue(null)
                    .build()
        }
    }

    static ArbitraryCustomizer<FieldError> fieldErrorFromOrderIdNotNull = new ArbitraryCustomizer<FieldError>() {
        @Override
        FieldError customizeFixture(FieldError fieldError) {
            return FieldError.builder()
                    .message("must not be null")
                    .messageTemplate("javax.validation.constraints.NotNull.message")
                    .field("orderId")
                    .invalidValue(null)
                    .build()
        }
    }

    static ArbitraryCustomizer<FieldError> fieldErrorFromNameNotBlank = new ArbitraryCustomizer<FieldError>() {
        @Override
        FieldError customizeFixture(FieldError fieldError) {
            return FieldError.builder()
                    .message("must not be blank")
                    .messageTemplate("javax.validation.constraints.NotBlank.message")
                    .field("name")
                    .invalidValue(null)
                    .build()
        }
    }

    static ArbitraryCustomizer<FieldError> fieldErrorFromTextNotBlank = new ArbitraryCustomizer<FieldError>() {
        @Override
        FieldError customizeFixture(FieldError fieldError) {
            return FieldError.builder()
                    .message("must not be blank")
                    .messageTemplate("javax.validation.constraints.NotBlank.message")
                    .field("text")
                    .invalidValue(null)
                    .build()
        }
    }

    static ArbitraryCustomizer<FieldError> fieldErrorFromCreatedAtNotNull = new ArbitraryCustomizer<FieldError>() {
        @Override
        FieldError customizeFixture(FieldError fieldError) {
            return FieldError.builder()
                    .message("must not be null")
                    .messageTemplate("javax.validation.constraints.NotNull.message")
                    .field("createdAt")
                    .invalidValue(null)
                    .build()
        }
    }

    static ArbitraryCustomizer<FieldError> fieldErrorFromTextMin = new ArbitraryCustomizer<FieldError>() {
        @Override
        FieldError customizeFixture(FieldError fieldError) {
            return FieldError.builder()
                    .message("size must be between 10 and 500")
                    .messageTemplate("javax.validation.constraints.Size.message")
                    .field("text")
                    .invalidValue("Texto")
                    .build()
        }
    }

    static ArbitraryCustomizer<FieldError> fieldErrorFromCreatedAtPastOrPresent = new ArbitraryCustomizer<FieldError>() {
        @Override
        FieldError customizeFixture(FieldError fieldError) {
            return FieldError.builder()
                    .message("must be a date in the past or in the present")
                    .messageTemplate("javax.validation.constraints.PastOrPresent.message")
                    .field("createdAt")
                    .invalidValue(LocalDateTime.now().plusDays(1))
                    .build()
        }
    }
}
