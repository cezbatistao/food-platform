package com.food.restaurant.entrypoint.rest.json

import com.food.restaurant.entrypoint.rest.json.error.ErrorDetailResponse

data class ErrorResponse(
        val error: ErrorDetailResponse
) {
}
