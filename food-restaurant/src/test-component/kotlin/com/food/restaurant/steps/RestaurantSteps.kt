package com.food.restaurant.steps

import com.food.restaurant.support.AbstractComponentTest
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.response.ValidatableResponse
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.skyscreamer.jsonassert.comparator.CustomComparator

class RestaurantSteps: AbstractComponentTest() {

    protected var category: String? = null

    @Given("I had a category {string}")
    fun `I had a category {string}`(category: String) {
        this.category = category
    }

    @When("user request a list of restaurant with category")
    fun `user request a list of restaurant with category`() {
        val response: Response = io.restassured.RestAssured.given()
                .contentType(ContentType.JSON)
                .param("category", this.category)
                .get("/api/v1/restaurants")

        this.testContext().setResponse(response)
    }

    @And("user gets a list of restaurants with content {string}")
    fun `user gets a list of restaurants with content {string}`(expectedJson: String) {
        val json: ValidatableResponse = this.testContext().getJson()

        val expected: String = this.getJsonStringExpectedFromClasspath(
                "data/restaurant/restaurants/response/${expectedJson}")
        val actual: String = this.getJsonStringActual(json)

        JSONAssert.assertEquals(expected, actual, CustomComparator(JSONCompareMode.LENIENT))
    }
}
