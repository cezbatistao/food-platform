package com.food.review.entrypoint.listener.resource

import com.fasterxml.jackson.annotation.JsonProperty

class DataResource<T>(
    @JsonProperty("data") var data: T
)
