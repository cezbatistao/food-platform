package com.food.restaurant.context

import com.food.restaurant.support.TestContext
import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.spring.CucumberContextConfiguration
import io.restassured.RestAssured
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("component-test")
class ContextLoader {

    @LocalServerPort
    private val serverPort = 0

    @Before
    fun startContext() {
        // Empty method, just to setup Application Test Context
        RestAssured.port = serverPort
    }

    @After
    fun tearDown() {
        TestContext.CONTEXT.reset()
    }
}
