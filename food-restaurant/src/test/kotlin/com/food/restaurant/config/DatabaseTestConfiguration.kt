package com.food.restaurant.config

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@SpringBootApplication
@ComponentScan(basePackages = arrayOf("com.food.restaurant.gateway"))
class DatabaseTestConfiguration {
}
