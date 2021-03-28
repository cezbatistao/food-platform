package com.food.restaurant.config

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@ActiveProfiles("it")
abstract class AbstractControllerIT {

    @Autowired
    lateinit var webApplicationContext: WebApplicationContext

    companion object {

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            FixtureFactoryLoader.loadTemplates("com.food.restaurant.templates")
        }
    }

    internal fun buildMockMvcWithBusinessExceptionHandler(): MockMvc {
        return MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .build()
    }
}
