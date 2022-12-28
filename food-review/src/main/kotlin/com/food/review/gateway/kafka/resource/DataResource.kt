package com.food.review.gateway.kafka.resource

import com.fasterxml.jackson.annotation.JsonProperty

class DataResource<T>(
    @JsonProperty("data") var data: T
)
