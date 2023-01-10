package com.food.review.config.feign

import feign.Logger
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED
import java.time.Duration
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.cloud.client.circuitbreaker.Customizer
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(basePackages = ["com.food.review.gateway.feign.client"])
class FeignConfiguration {

//    @Bean
//    fun feignLoggerLevel(): Logger.Level {
//        return Logger.Level.BASIC
//    }
//
//    @Bean
//    fun errorDecoder(): SpringWebClientErrorDecoder {
//        return SpringWebClientErrorDecoder()
//    }
//
//    @Bean
//    fun circuitBreakerFactoryCustomizer(): Customizer<Resilience4JCircuitBreakerFactory> {
//        val cbConfig = CircuitBreakerConfig.custom()
//            .slidingWindowType(COUNT_BASED)
//            .slidingWindowSize(5)
//            .failureRateThreshold(20.0f)
//            .waitDurationInOpenState(Duration.ofSeconds(5))
//            .permittedNumberOfCallsInHalfOpenState(5)
//            .build()
//        return Customizer<Resilience4JCircuitBreakerFactory> { resilience4JCircuitBreakerFactory ->
//            resilience4JCircuitBreakerFactory.configure(
//                { builder -> builder.circuitBreakerConfig(cbConfig) },
//                "UserSessionClient#validateSession(UUID)"
//            )
//        }
//    }
}