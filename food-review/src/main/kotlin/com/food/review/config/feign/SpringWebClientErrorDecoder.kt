package com.food.review.config.feign

import com.google.common.collect.Lists
import com.google.common.io.ByteStreams
import feign.Response
import feign.codec.ErrorDecoder
import feign.codec.ErrorDecoder.Default
import java.io.IOException
import java.nio.charset.Charset
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException

class SpringWebClientErrorDecoder {

    private val delegate: ErrorDecoder = Default()

    fun decode(methodKey: String?, response: Response): Exception? {
        val statusCode: HttpStatus = HttpStatus.valueOf(response.status())
        val charset = Charset.forName("UTF-8")
        if (statusCode.is4xxClientError) {
            return HttpClientErrorException(
                statusCode,
                response.reason(),
                responseHeaders(response),
                responseBody(response),
                charset
            )
        }
        return if (statusCode.is5xxServerError) {
            HttpServerErrorException(
                statusCode,
                response.reason(),
                responseHeaders(response),
                responseBody(response),
                charset
            )
        } else delegate.decode(methodKey, response)
    }

    private fun responseHeaders(response: Response): HttpHeaders? {
        val headers = HttpHeaders()
        response
            .headers()
            .forEach { entry -> headers[entry.key] = Lists.newArrayList(entry.value) }
        return headers
    }

    private fun responseBody(response: Response): ByteArray {
        return try {
            ByteStreams.toByteArray(response.body().asInputStream())
        } catch (e: IOException) {
            throw HttpMessageNotReadableException("Failed to process response body.", e)
        }
    }
}