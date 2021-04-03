package com.food.restaurant.steps

import com.food.restaurant.support.AbstractComponentTest
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.response.ValidatableResponse
import org.junit.jupiter.api.Assertions
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.skyscreamer.jsonassert.comparator.CustomComparator

class RestaurantSteps: AbstractComponentTest() {

    protected var category: String? = null

    private var response: Response? = null
    private var json: ValidatableResponse? = null

    @Given("I had a category {string}")
    fun `I had a category {string}`(category: String) {
        this.category = category
    }

    @When("user request a list of restaurant with category")
    fun `user request a list of restaurant with category`() {
        this.response = io.restassured.RestAssured.given()
                .contentType(ContentType.JSON)
                .param("category", this.category)
                .get("/api/v1/restaurants")
    }

    @Then("user get success response from restaurant")
    fun `user get success response`() {
        this.json = this.response?.then()?.statusCode(200);
    }

    @And("user gets a list of restaurants")
    fun `user gets a list of restaurants`() {
        Assertions.assertNotNull(this.json)
        val expected: String = this.getJsonStringExpectedFromClasspath(
                "data/restaurant/restaurants/response/200-restaurants.json")
        val actual: String = this.getJsonStringActual(this.json)

        JSONAssert.assertEquals(expected, actual, CustomComparator(JSONCompareMode.LENIENT))
    }
}
