package com.food.review.gateway.mongo.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Document("reviews")
public class ReviewMongo implements Serializable {

    private static final long serialVersionUID = -2955603894608116500L;

    @Id
    private UUID id;

    private UUID orderId;
    private String username;
    private String name;
    private String text;
    private LocalDateTime createdAt;

}
