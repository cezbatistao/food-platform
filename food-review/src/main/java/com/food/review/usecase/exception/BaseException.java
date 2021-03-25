package com.food.review.usecase.exception;

import lombok.Getter;

public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 2245102753811710377L;

    @Getter
    private String code;

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
