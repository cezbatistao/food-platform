package com.food.restaurant.entrypoint.rest

import com.food.restaurant.entrypoint.rest.json.ErrorResponse
import com.food.restaurant.entrypoint.rest.json.error.ErrorDetailResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

class CustomExceptionHandler {

    @ExceptionHandler(Exception::class)
    @ResponseBody
    fun processException(ex: Exception): ResponseEntity<ErrorResponse> {
//        log.error("An unexpected error occured: {}", ex.message, ex)
        return this.buildErrorReponse(HttpStatus.INTERNAL_SERVER_ERROR, "9999", ex)
    }

    private fun buildErrorReponse(status: HttpStatus, code: String, ex: Exception): ResponseEntity<ErrorResponse> {
        return this.buildErrorReponse(status, code, ex.message!!)
    }

    private fun buildErrorReponse(status: HttpStatus, code: String, message: String): ResponseEntity<ErrorResponse> {
        val headers = HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        return ResponseEntity.status(status)
                .headers(headers)
                .body(ErrorResponse(ErrorDetailResponse(code, message)))
    }
}
