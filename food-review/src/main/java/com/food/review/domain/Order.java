package com.food.review.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(exclude = {"id"})
@EqualsAndHashCode
public class Order implements Serializable {

    private static final long serialVersionUID = 4617741190503642139L;

    private UUID id;
    private Restaurant restaurant;

    @Singular
    private List<OrderItem> orderItems;

    private BigDecimal total;

}
