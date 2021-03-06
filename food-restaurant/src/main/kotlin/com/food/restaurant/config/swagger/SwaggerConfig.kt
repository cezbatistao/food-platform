package com.food.restaurant.config.swagger

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors.regex
import springfox.documentation.builders.RequestHandlerSelectors.basePackage
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType.SWAGGER_2
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.UiConfiguration
import springfox.documentation.swagger.web.UiConfigurationBuilder
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
@ComponentScan(basePackages = arrayOf("io.swagger"))
class SwaggerConfig {

    @Value("\${info.app.name}")
    private lateinit var applicationName: String

    @Value("\${info.app.description}")
    private lateinit var applicationDescription: String

    @Value("\${info.app.version}")
    private lateinit var applicationVersion: String

    @Value("\${application.swagger.apis.base-package}")
    private lateinit var apisBasePackage: String

    @Value("\${application.swagger.apis.path}")
    private lateinit var apisPath: String

    @Bean
    fun documentation(): Docket {
        return Docket(SWAGGER_2)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .select()
                .apis(basePackage(apisBasePackage))
                .paths(regex(apisPath))
                .build()
    }

    @Bean
    fun uiConfig(): UiConfiguration {
        return UiConfigurationBuilder.builder()
                .displayRequestDuration(true)
                .validatorUrl(null as String?)
                .build()
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
                .title(applicationName)
                .description(applicationDescription)
                .version(applicationVersion)
                .build()
    }
}
