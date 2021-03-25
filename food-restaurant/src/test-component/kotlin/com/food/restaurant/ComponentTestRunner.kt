package com.food.restaurant

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader
import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import net.masterthought.cucumber.Configuration
import net.masterthought.cucumber.ReportBuilder
import net.masterthought.cucumber.presentation.PresentationMode
import net.masterthought.cucumber.sorting.SortingMethod
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.testcontainers.containers.MySQLContainer
import java.io.File
import java.nio.file.Paths

@RunWith(Cucumber::class)
@CucumberOptions(
        plugin = arrayOf("pretty", "json:build/reports/tests/componentTest/jsonReports/cucumber.json"),
        features = arrayOf("src/test-component/resources/features"),
        glue = arrayOf("com.food.restaurant"),
        tags = "not @Ignore"
)
class ComponentTestRunner {

    internal class SpecifiedMySQLContainer(image: String) : MySQLContainer<SpecifiedMySQLContainer>(image)

    companion object {
        private val cucumberJsonFile: Array<String> = arrayOf("build/reports/tests/componentTest/jsonReports/cucumber.json")

        @JvmStatic
        @BeforeClass
        internal fun beforeClass() {
            FixtureFactoryLoader.loadTemplates("com.food.restaurant.templates")

            val mysqlContainer = SpecifiedMySQLContainer("mysql:5.7")
                    .withDatabaseName("db_tce_restaurant")
                    .withUsername("restaurant_tce_user")
                    .withPassword("restaurant_tce_passwd")
                    .withExposedPorts(3306)

            mysqlContainer.start()

            System.setProperty("spring.datasource.component-test.restaurant.url.container", mysqlContainer.getJdbcUrl())
            System.setProperty("spring.datasource.component-test.restaurant.username.container", mysqlContainer.getUsername())
            System.setProperty("spring.datasource.component-test.restaurant.password.container", mysqlContainer.getPassword())
        }

        @JvmStatic
        @AfterClass
        internal fun afterClass() {
            if (cucumberJsonFile.none { file -> Paths.get(file).toFile().exists() }) {
                logger.warn("No report generated because json files not configured")
            } else {
                val reportOutputDirectory = File("build/reports/tests/componentTest")
                val configuration = Configuration(reportOutputDirectory, "Component Tests Report")
                configuration.setSortingMethod(SortingMethod.NATURAL)
                configuration.addPresentationModes(PresentationMode.EXPAND_ALL_STEPS)
                val reportBuilder = ReportBuilder(cucumberJsonFile.toMutableList(), configuration)
                reportBuilder.generateReports()
            }
        }
    }
}
