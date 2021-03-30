package com.food.restaurant.entrypoint.rest.json

import com.fasterxml.jackson.annotation.JsonProperty

data class DataResponse<T>(
        @get:JsonProperty("data") val t: T
) {
}
