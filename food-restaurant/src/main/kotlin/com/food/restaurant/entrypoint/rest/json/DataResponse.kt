package com.food.restaurant.entrypoint.rest.json

import com.fasterxml.jackson.annotation.JsonProperty

data class DataResponse<T>(
        @JsonProperty("data") val t: T
) {
}
