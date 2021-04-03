package com.food.restaurant.steps

import com.food.restaurant.support.AbstractComponentTest
import io.cucumber.java.en.And
import io.cucumber.java.en.When
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.response.ValidatableResponse
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.skyscreamer.jsonassert.comparator.CustomComparator

class CategorySteps: AbstractComponentTest() {

    @When("user request all categories")
    fun `user request all categories`() {
        val response: Response = io.restassured.RestAssured.given()
                .contentType(ContentType.JSON)
                .get("/api/v1/categories")

        this.testContext().setResponse(response)
    }

    @And("user gets categories from restaurants")
    fun `user gets categories from restaurants`() {
        val json: ValidatableResponse = this.testContext().getJson()

        val expected: String = this.getJsonStringExpectedFromClasspath(
                "data/restaurant/categories/response/200-categories.json")
        val actual: String = this.getJsonStringActual(json)

        JSONAssert.assertEquals(expected, actual, CustomComparator(JSONCompareMode.LENIENT))
    }
}
