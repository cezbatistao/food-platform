package com.food.restaurant.config

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader
import com.food.restaurant.config.database.DatabaseConfiguration
import org.junit.jupiter.api.BeforeAll
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.containers.MySQLContainer

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(
        DatabaseTestConfiguration::class, DatabaseConfiguration::class
))
@ActiveProfiles("it")
@Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = arrayOf("classpath:/db/clean-db.sql")
)
abstract class AbstractRepositoryIT {

    internal class SpecifiedMySQLContainer(val image: String) : MySQLContainer<SpecifiedMySQLContainer>(image)

    companion object {

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            val mysqlContainer = SpecifiedMySQLContainer("mysql:5.7")
                    .withDatabaseName("db_restaurant_it")
                    .withUsername("restaurant_user_it")
                    .withPassword("restaurant_passwd_it")
                    .withExposedPorts(3306)

            mysqlContainer.start()

            FixtureFactoryLoader.loadTemplates("com.food.restaurant.templates")

            System.setProperty("spring.datasource.restaurant.url.container", mysqlContainer.getJdbcUrl())
            System.setProperty("spring.datasource.restaurant.username.container", mysqlContainer.getUsername())
            System.setProperty("spring.datasource.restaurant.password.container", mysqlContainer.getPassword())
        }
    }
}
