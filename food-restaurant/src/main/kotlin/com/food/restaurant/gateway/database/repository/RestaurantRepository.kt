package com.food.restaurant.gateway.database.repository

import com.food.restaurant.domain.Category
import com.food.restaurant.gateway.database.model.RestaurantModel
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory
import org.simpleflatmapper.jdbc.spring.ResultSetExtractorImpl
import org.simpleflatmapper.jdbc.spring.SqlParameterSourceFactory
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository

@Repository
class RestaurantRepository(
        private val jdbcTemplate: NamedParameterJdbcOperations
) {

    fun findAllByCategory(category: Category): List<RestaurantModel> {
        val resultSetExtractor: ResultSetExtractorImpl<RestaurantModel> = JdbcTemplateMapperFactory
                .newInstance()
                .newResultSetExtractor(RestaurantModel::class.java)

        val restaurants: MutableList<RestaurantModel> = this.jdbcTemplate.query(
                """
                   SELECT re.id, 
                          re.name, 
                          ca.id AS category_id, 
                          ca.code AS category_code, 
                          ca.description AS category_description, 
                          re.logo, 
                          re.description, 
                          re.address 
                   FROM restaurant re 
                   INNER JOIN category ca ON ca.id = re.category_id 
                   WHERE ca.code = :categoryCode 
                """.trimIndent(),
                mapOf<String, Any?>("categoryCode" to category.code),
                resultSetExtractor) as MutableList<RestaurantModel>

        return restaurants.toList()
    }

    fun saveAll(restaurants: List<RestaurantModel>) {
        val parameterSourceFactory: SqlParameterSourceFactory<RestaurantModel> = JdbcTemplateMapperFactory
                .newInstance()
                .newSqlParameterSourceFactory(RestaurantModel::class.java)

        this.jdbcTemplate.batchUpdate(
                """
                   INSERT INTO restaurant(name, category_id, logo, description, address) 
                   VALUES(:name, :category_id, :logo, :description, :address) 
                """.trimIndent(),
                parameterSourceFactory.newSqlParameterSources(restaurants))
    }
}
