package com.food.review.entrypoint.rest.json.response

import com.fasterxml.jackson.annotation.JsonProperty

data class DataResponse<T>(
    @get:JsonProperty("data") val t: T
)
