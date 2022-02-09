package com.food.review.gateway;

import com.food.review.domain.Order;

import java.util.UUID;

public interface OrderGateway {

    Order getById(UUID uuid);

}
