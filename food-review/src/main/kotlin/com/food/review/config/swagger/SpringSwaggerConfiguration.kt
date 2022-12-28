package com.food.review.config.swagger

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springdoc.core.GroupedOpenApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpringSwaggerConfiguration {

    private val BASE_PACKAGE = "com.food.review.entrypoint.rest"

    @Value("\${spring.application.name}")
    private val applicationName: String? = null

    @Value("\${spring.application.description:}")
    private val applicationDescription: String? = null

    @Value("\${spring.application.version:}")
    private val applicationVersion: String? = null

    @Value("\${spring.profiles.active:}")
    private val profile: String? = null

    @Bean
    fun publicApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
                .group(applicationName)
                .pathsToMatch("/api/**")
                .build()
    }

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
                .info(Info().title(applicationName)
                        .description(applicationDescription)
                        .version(applicationVersion)
                        .license(License().name("Apache 2.0").url("http://springdoc.org")))
    }
}
