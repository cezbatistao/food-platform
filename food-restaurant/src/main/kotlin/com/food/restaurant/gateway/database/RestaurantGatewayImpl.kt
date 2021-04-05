package com.food.restaurant.gateway.database

import com.food.restaurant.domain.Category
import com.food.restaurant.domain.MenuItem
import com.food.restaurant.domain.Restaurant
import com.food.restaurant.domain.RestaurantDetail
import com.food.restaurant.domain.exception.EntityNotFoundException
import com.food.restaurant.gateway.RestaurantGateway
import com.food.restaurant.gateway.database.model.RestaurantModel
import com.food.restaurant.gateway.database.repository.RestaurantRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class RestaurantGatewayImpl(
        private val restaurantRepository: RestaurantRepository
): RestaurantGateway {

    override fun findAllByCategory(category: Category): List<Restaurant> {
        return this.restaurantRepository.findAllByCategory(category).map {
            Restaurant(
                    it.id,
                    UUID.fromString(it.uuid),
                    it.name,
                    Category(
                            it.category.id,
                            UUID.fromString(it.category.uuid),
                            it.category.code,
                            it.category.description),
                    it.logo,
                    it.description,
                    it.address
            )
        }
    }

    override fun findByUuid(uuid: UUID): RestaurantDetail {
        val restaurantModel: RestaurantModel = this.restaurantRepository.findByUuid(uuid)
                ?: throw EntityNotFoundException("0003", "entityNotFoundException", "Restaurant ${uuid} don't exists")

        return this.mapper(restaurantModel)
    }

    private fun mapper(restaurantModel: RestaurantModel): RestaurantDetail {
        return RestaurantDetail(
                restaurantModel.id,
                UUID.fromString(restaurantModel.uuid),
                restaurantModel.name,
                Category(
                        restaurantModel.category.id,
                        UUID.fromString(restaurantModel.category.uuid),
                        restaurantModel.category.code,
                        restaurantModel.category.description),
                restaurantModel.logo,
                restaurantModel.description,
                restaurantModel.address,
                restaurantModel.itens?.map { menuItemModel ->
                    MenuItem(
                            menuItemModel.id,
                            UUID.fromString(menuItemModel.uuid),
                            menuItemModel.name,
                            menuItemModel.description,
                            menuItemModel.value
                    )
                }!!
        )
    }
}
