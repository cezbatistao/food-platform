package com.food.review.usecase.exception;

import com.food.review.domain.error.FieldError;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 6195373487710917070L;

    @Getter
    private final transient List<FieldError> fieldErrors = new ArrayList<>();

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, List<FieldError> constraintsErrors) {
        super(message);
        this.fieldErrors.addAll(constraintsErrors);
    }
}
