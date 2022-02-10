package com.food.review.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(exclude = {"id"})
@EqualsAndHashCode
public class OrderItem implements Serializable {

    private static final long serialVersionUID = -4638841846509670495L;

    private UUID id;
    private String name;
    private Integer amount;
    private BigDecimal unitValue;

}
