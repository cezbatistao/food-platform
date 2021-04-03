package com.food.restaurant.steps

import com.food.restaurant.support.AbstractComponentTest
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.response.ValidatableResponse
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.skyscreamer.jsonassert.comparator.CustomComparator

class CategorySteps: AbstractComponentTest() {

    private var response: Response? = null
    private var json: ValidatableResponse? = null

    @When("user request all categories")
    fun userRequestAllCategories() {
        this.response = io.restassured.RestAssured.given()
                .contentType(ContentType.JSON)
                .get("/api/v1/categories");
    }

    @Then("user get success response")
    fun `user get success response`() {
        this.json = this.response?.then()?.statusCode(200);
    }

    @Then("user gets categories from restaurants")
    fun userGetsCategoriesFromRestaurants() {
        val expected: String = this.getJsonStringExpectedFromClasspath(
                "data/restaurant/categories/response/200-categories.json")
        val actual: String = this.getJsonStringActual(this.json)

        JSONAssert.assertEquals(expected, actual, CustomComparator(JSONCompareMode.LENIENT))
    }
}
