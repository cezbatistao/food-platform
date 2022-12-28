package com.food.review.entrypoint.rest.json.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ListDataResponse<T>(
    @get:JsonProperty("data") val t: List<T>,
    val page: Int,
    @get:JsonProperty("total_pages") val totalPages: Int,
    val size: Int,
    val total: Long
)
