package com.food.review.entrypoint.rest.json.response.error

data class ErrorDetailResponse(
        val code: String,
        val message: String
) {
        val app: String = "review-app"
}
