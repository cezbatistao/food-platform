package com.food.restaurant.support

import io.restassured.response.Response
import io.restassured.response.ValidatableResponse

// https://medium.com/@bcarunmail/sharing-state-between-cucumber-step-definitions-using-java-and-spring-972bc31117af
enum class TestContext {

    CONTEXT;

    private val RESPONSE = "RESPONSE"
    private val JSON = "JSON"

    private val testContexts: HashMap<String, Any> = HashMap()

    open operator fun <T> get(name: String): T {
        return testContexts.get(name) as T
    }

    open operator fun <T> set(name: String, value: T): T {
        testContexts.put(name, value as Any)
        return value
    }

    open fun getResponse(): Response {
        return this.get<Response>(RESPONSE)
    }

    open fun setResponse(response: Response): Response {
        return this.set(RESPONSE, response)
    }

    open fun getJson(): ValidatableResponse {
        return this.get(JSON)
    }

    open fun setJson(json: ValidatableResponse): ValidatableResponse {
        return this.set(JSON, json)
    }

    open fun reset() {
        testContexts.clear()
    }
}
