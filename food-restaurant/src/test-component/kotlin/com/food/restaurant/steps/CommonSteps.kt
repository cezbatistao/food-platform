package com.food.restaurant.steps

import com.food.restaurant.support.AbstractComponentTest
import io.cucumber.java.en.Then
import org.junit.jupiter.api.Assertions
import io.restassured.response.Response
import io.restassured.response.ValidatableResponse

class CommonSteps: AbstractComponentTest() {

    @Then("user get success response")
    fun `user get success response`() {
        val response: Response = this.testContext().getResponse();
        Assertions.assertNotNull(response)
        val json: ValidatableResponse = response.then().statusCode(200)

        this.testContext().setJson(json)
    }
}