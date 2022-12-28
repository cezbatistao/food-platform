package com.food.review.lang

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.IfProfileValue
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@ExtendWith(SpringExtension::class)
@WebAppConfiguration
@ActiveProfiles("component-test")
@IfProfileValue(name = "spring.profiles.active", value = "component-test")
abstract class ControllerComponentTest {

    @Autowired
    lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    internal fun buildMockMvcWithBusinessExceptionHandler(): MockMvc {
        return MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
            .build()
    }
}
