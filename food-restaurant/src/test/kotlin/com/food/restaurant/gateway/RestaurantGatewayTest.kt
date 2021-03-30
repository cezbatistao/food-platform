package com.food.restaurant.gateway

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.Rule
import com.food.restaurant.config.AbstractRepositoryIT
import com.food.restaurant.domain.Category
import com.food.restaurant.domain.Restaurant
import com.food.restaurant.gateway.database.model.CategoryModel
import com.food.restaurant.gateway.database.model.RestaurantModel
import com.food.restaurant.gateway.database.repository.CategoryRepository
import com.food.restaurant.gateway.database.repository.RestaurantRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class RestaurantGatewayTest: AbstractRepositoryIT() {

    @Autowired
    lateinit var restaurantGateway: RestaurantGateway

    @Autowired
    lateinit var restaurantRepository: RestaurantRepository

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @Test
    fun `should return empty list when database is empty`() {
        // given
        val categoryModel: CategoryModel = Fixture.from(CategoryModel::class.java).gimme(
                "category pizza")
        this.categoryRepository.saveAll(listOf(categoryModel))

        val category = Category(categoryModel.id, categoryModel.code, categoryModel.description)

        // when
        val restaurants: List<Restaurant> = restaurantGateway.findAllByCategory(category)

        // then
        Assertions.assertNotNull(restaurants)
        Assertions.assertEquals(0, restaurants.size)
    }

    @Test
    fun `should return a list of restaurants from a category`() {
        // given
        var categoryModel: CategoryModel = Fixture.from(CategoryModel::class.java).gimme(
                "category pizza")
        this.categoryRepository.saveAll(listOf(categoryModel))
        categoryModel = this.categoryRepository.findByCode(categoryModel.code)!!

        val category = Category(categoryModel.id, categoryModel.code, categoryModel.description)

        val restaurantsModel: List<RestaurantModel> = Fixture.from(RestaurantModel::class.java)
                .gimme<RestaurantModel>(
                        3, "pizza hut", "braz pizzaria", "dominos pizza").map {
                    RestaurantModel(it.id, it.name, categoryModel, it.logo, it.description, it.address)
                }
        restaurantRepository.saveAll(restaurantsModel)

        // when
        val restaurants: List<Restaurant> = restaurantGateway.findAllByCategory(category)

        // then
        Assertions.assertNotNull(restaurants)
        Assertions.assertEquals(3, restaurants.size)

        // and
        restaurantsModel.forEach { restaurantModel ->
            val restaurant: Restaurant = restaurants.find { it.name == restaurantModel.name }!!
            Assertions.assertNotNull(restaurant.id)
            Assertions.assertEquals(restaurantModel.category.id, restaurant.category.id)
            Assertions.assertEquals(restaurantModel.category.code, restaurant.category.code)
            Assertions.assertEquals(restaurantModel.category.description, restaurant.category.description)
            Assertions.assertEquals(restaurantModel.logo, restaurant.logo)
            Assertions.assertEquals(restaurantModel.description, restaurant.description)
            Assertions.assertEquals(restaurantModel.address, restaurant.address)
        }
    }
}
