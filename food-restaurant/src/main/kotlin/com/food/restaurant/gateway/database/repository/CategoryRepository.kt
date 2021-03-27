package com.food.restaurant.gateway.database.repository

import com.food.restaurant.gateway.database.model.CategoryModel
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory
import org.simpleflatmapper.jdbc.spring.ResultSetExtractorImpl
import org.simpleflatmapper.jdbc.spring.SqlParameterSourceFactory
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository


@Repository
class CategoryRepository(
        private val jdbcTemplate: NamedParameterJdbcOperations
) {

    fun listAll(): List<CategoryModel> {
        val resultSetExtractor: ResultSetExtractorImpl<CategoryModel> = JdbcTemplateMapperFactory.newInstance()
                .newResultSetExtractor(CategoryModel::class.java)

        val categories: MutableList<CategoryModel> = this.jdbcTemplate.query(
                """
                   SELECT id, 
                          code, 
                          description 
                   FROM category 
                """.trimIndent(),
                mapOf<String, Any?>(),
                resultSetExtractor) as MutableList<CategoryModel>

        return categories.toList()
    }

    fun saveAll(categoriesModel: List<CategoryModel>) {
        val parameterSourceFactory: SqlParameterSourceFactory<CategoryModel> = JdbcTemplateMapperFactory
                .newInstance()
                .newSqlParameterSourceFactory<CategoryModel>(CategoryModel::class.java)

        this.jdbcTemplate.batchUpdate(
                """
                   INSERT INTO category(code, description) 
                   VALUES(:code, :description) 
                """.trimIndent(),
                parameterSourceFactory.newSqlParameterSources(categoriesModel))
    }
}