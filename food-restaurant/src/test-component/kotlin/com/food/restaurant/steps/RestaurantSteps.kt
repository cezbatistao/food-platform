package com.food.restaurant.steps

import com.food.restaurant.support.AbstractComponentTest
import io.cucumber.java.en.And
import io.cucumber.java.en.And.Ands
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
    protected var restaurantUuid: String? = null

    @Given("I had a {string} category")
    fun `I had a category {string}`(category: String) {
        this.category = category
    }

    @Given("I had unique id {string} from restaurant")
    fun `I had unique id from restaurant {string}`(restaurantUuid: String) {
        this.restaurantUuid = restaurantUuid
    }

    @When("user request a list of restaurant with category")
    fun `user request a list of restaurant with category`() {
        val response: Response = io.restassured.RestAssured.given()
                .contentType(ContentType.JSON)
                .param("category", this.category)
                .get("/api/v1/restaurants")

        this.testContext().setResponse(response)
    }

    @When("user request for detail restaurant with unique id")
    fun `user request a detail restaurant with a unique id`() {
        val response: Response = io.restassured.RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("uuid", this.restaurantUuid)
                .get("/api/v1/restaurants/{uuid}")

        this.testContext().setResponse(response)
    }

    @Ands(
            And("user gets a list of restaurants with content {string}"),
            And("user gets a detail from restaurant {string}"),
            And("user gets a error detail from restaurant {string}")
    )
    fun `verify response payload {string}`(expectedJson: String) {
        val json: ValidatableResponse = this.testContext().getJson()

        val expected: String = this.getJsonStringExpectedFromClasspath(
                "data/restaurant/restaurants/response/${expectedJson}")
        val actual: String = this.getJsonStringActual(json)

        JSONAssert.assertEquals(expected, actual, CustomComparator(JSONCompareMode.LENIENT))
    }
}
