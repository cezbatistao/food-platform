package com.food.restaurant.gateway.database.repository

import com.food.restaurant.domain.Category
import com.food.restaurant.gateway.database.model.RestaurantModel
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory
import org.simpleflatmapper.jdbc.spring.ResultSetExtractorImpl
import org.simpleflatmapper.jdbc.spring.SqlParameterSourceFactory
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import java.math.BigInteger
import java.util.*


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
                          re.uuid, 
                          re.name, 
                          ca.id AS category_id, 
                          ca.uuid AS category_uuid, 
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

    fun findByUuid(uuid: UUID): RestaurantModel? {
        val resultSetExtractor: ResultSetExtractorImpl<RestaurantModel> = JdbcTemplateMapperFactory
                .newInstance()
                .addKeys("id", "itens_id")
                .newResultSetExtractor(RestaurantModel::class.java)

        val restaurants: MutableList<RestaurantModel> = this.jdbcTemplate.query(
                """
                   SELECT re.id,
                          re.uuid, 
                          re.name, 
                          ca.id AS category_id, 
                          ca.uuid AS category_uuid, 
                          ca.code AS category_code, 
                          ca.description AS category_description, 
                          re.logo, 
                          re.description, 
                          re.address, 
                          mi.id AS itens_id, 
                          mi.uuid AS itens_uuid, 
                          mi.name AS itens_name, 
                          mi.description AS itens_description, 
                          mi.price AS itens_value 
                   FROM restaurant re 
                   INNER JOIN category ca ON ca.id = re.category_id 
                   INNER JOIN restaurant_menu_item mi ON mi.restaurant_id = re.id 
                   WHERE re.uuid = :uuid 
                """.trimIndent(),
                mapOf<String, Any?>("uuid" to uuid.toString()),
                resultSetExtractor) as MutableList<RestaurantModel>

        return restaurants.firstOrNull()
    }

    fun saveAll(restaurants: List<RestaurantModel>) {
        val parameterSourceFactory: SqlParameterSourceFactory<RestaurantModel> = JdbcTemplateMapperFactory
                .newInstance()
                .newSqlParameterSourceFactory(RestaurantModel::class.java)

        restaurants.map { restaurantModel ->
            val keyHolder: KeyHolder = GeneratedKeyHolder()

            this.jdbcTemplate.update(
                    """
                       INSERT INTO restaurant(uuid, name, category_id, logo, description, address)
                       VALUES(:uuid, :name, :category_id, :logo, :description, :address)
                    """.trimIndent(),
                    parameterSourceFactory.newSqlParameterSource(restaurantModel),
                    keyHolder
            )

            val restaurantId: Long = keyHolder.getKeyAs(BigInteger::class.java)?.toLong()!!

            val map: Array<MutableMap<String, Any>>? = restaurantModel.itens?.map {
                mutableMapOf<String, Any>(
                        "uuid" to it.uuid,
                        "name" to it.name,
                        "description" to it.description,
                        "price" to it.value,
                        "restaurant_id" to restaurantId
                )
            }?.toTypedArray()

            this.jdbcTemplate.batchUpdate(
                    """
                       INSERT INTO restaurant_menu_item(uuid, name, description, price, restaurant_id)
                       VALUES(:uuid, :name, :description, :price, :restaurant_id)
                    """.trimIndent(),
                    map!!)
        }
    }
}
