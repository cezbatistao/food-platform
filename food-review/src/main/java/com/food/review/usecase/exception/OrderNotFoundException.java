package com.food.review.usecase.exception;

import lombok.Getter;

import java.util.UUID;

public class OrderNotFoundException extends BaseException {

    private static final long serialVersionUID = -1149764179744360283L;

    @Getter
    private UUID orderId;

    public OrderNotFoundException(UUID orderId, String code, String message) {
        super(code, message);
        this.orderId = orderId;
    }

    public OrderNotFoundException(UUID orderId, String code, String message, Throwable cause) {
        super(code, message, cause);
        this.orderId = orderId;
    }
}
