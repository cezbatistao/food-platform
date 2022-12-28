package com.food.review.entrypoint.rest.json.response

import com.food.review.entrypoint.rest.json.response.error.ErrorDetailResponse

data class ErrorResponse(
        val error: ErrorDetailResponse
)
