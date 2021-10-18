package com.food.restaurant.entrypoint.rest

import com.food.restaurant.domain.exception.EntityNotFoundException
import com.food.restaurant.entrypoint.rest.json.ErrorResponse
import com.food.restaurant.entrypoint.rest.json.error.ErrorDetailResponse
import com.food.restaurant.logger
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException


@ControllerAdvice
class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseBody
    fun processException(ex: ConstraintViolationException): ResponseEntity<ErrorResponse> {
        logger.warn("An not found entity occured: {}", ex.message, ex)

        val constraintViolation: ConstraintViolation<*> = ex.constraintViolations.iterator().next()
        val propertyName: String = constraintViolation.propertyPath.toString()
        val message = constraintViolation.message

        return this.buildErrorReponse(HttpStatus.BAD_REQUEST, "0001", "Parameter ${propertyName} ${message}")
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseBody
    fun processException(ex: EntityNotFoundException): ResponseEntity<ErrorResponse> {
        logger.warn("An not found entity occured: {}", ex.message, ex)
        return this.buildErrorReponse(HttpStatus.NOT_FOUND, ex.code, ex.message!!)
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    @ResponseBody
    fun processException(ex: Exception): ResponseEntity<ErrorResponse> {
        logger.error("An unexpected error occured: {}", ex.message, ex)
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
