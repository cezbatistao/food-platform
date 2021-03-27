package com.food.restaurant.config.database

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.TransactionManager
import javax.sql.DataSource

@Configuration
@EnableJdbcRepositories("com.food.restaurant.gateway.database")
class DatabaseConfiguration: AbstractJdbcConfiguration() {

    @Bean
    fun namedParameterJdbcOperations(@Autowired dataSource: DataSource): NamedParameterJdbcOperations {
        return NamedParameterJdbcTemplate(dataSource)
    }

    @Bean
    fun transactionManager(@Autowired dataSource: DataSource): TransactionManager {
        return DataSourceTransactionManager(dataSource);
    }
}
