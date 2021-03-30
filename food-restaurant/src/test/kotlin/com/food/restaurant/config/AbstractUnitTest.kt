package com.food.restaurant.config

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader
import org.junit.jupiter.api.BeforeAll

abstract class AbstractUnitTest {

    companion object {

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            FixtureFactoryLoader.loadTemplates("com.food.restaurant.templates")
        }
    }
}
