package com.food.restaurant.gateway

import br.com.six2six.fixturefactory.Fixture
import com.food.restaurant.config.AbstractRepositoryIT
import com.food.restaurant.domain.Category
import com.food.restaurant.domain.Restaurant
import com.food.restaurant.domain.exception.EntityNotFoundException
import com.food.restaurant.gateway.database.model.CategoryModel
import com.food.restaurant.gateway.database.model.RestaurantModel
import com.food.restaurant.gateway.database.repository.CategoryRepository
import com.food.restaurant.gateway.database.repository.RestaurantRepository
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.util.*

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

        val category = Category(categoryModel.id, UUID.fromString(categoryModel.uuid),
                categoryModel.code, categoryModel.description)

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

        val category = Category(categoryModel.id, UUID.fromString(categoryModel.uuid),
                categoryModel.code, categoryModel.description)

        val restaurantsModel: List<RestaurantModel> = Fixture.from(RestaurantModel::class.java)
                .gimme<RestaurantModel>(
                        3, "pizza hut", "braz pizzaria", "dominos pizza").map {
                    RestaurantModel(it.id, it.uuid, it.name, categoryModel, it.logo, it.description, it.address, it.itens)
                }
        restaurantRepository.saveAll(restaurantsModel)

        // when
        val restaurants: List<Restaurant> = restaurantGateway.findAllByCategory(category)

        // then
        Assertions.assertNotNull(restaurants)
        Assertions.assertEquals(3, restaurants.size)

        // and
        restaurantsModel.forEach { restaurantModel ->
            val restaurant: Restaurant = restaurants.find { it.uuid.toString() == restaurantModel.uuid }!!
            Assertions.assertNotNull(restaurant.id)
            Assertions.assertEquals(restaurantModel.name, restaurant.name)
            Assertions.assertEquals(restaurantModel.category.id, restaurant.category.id)
            Assertions.assertEquals(restaurantModel.category.uuid, restaurant.category.uuid.toString())
            Assertions.assertEquals(restaurantModel.category.code, restaurant.category.code)
            Assertions.assertEquals(restaurantModel.category.description, restaurant.category.description)
            Assertions.assertEquals(restaurantModel.logo, restaurant.logo)
            Assertions.assertEquals(restaurantModel.description, restaurant.description)
            Assertions.assertEquals(restaurantModel.address, restaurant.address)
        }
    }

    @Test
    fun `should throw a entity not found exception`() {
        // given
        val uuidPizzaHut = UUID.fromString("cbb9c2bd-abde-48a3-891a-6229fc9b7c2f")

        // when
        val exception: EntityNotFoundException = Assertions.assertThrows(EntityNotFoundException::class.java) {
            restaurantGateway.findByUuid(uuidPizzaHut)
        }

        // then
        Assertions.assertNotNull(exception)
        Assertions.assertEquals("0003", exception.code)
        Assertions.assertEquals("entityNotFoundException", exception.error)
        Assertions.assertEquals("Restaurant ${uuidPizzaHut.toString()} don't exists", exception.message)
    }

    @Test
    fun `should return a restaurant with menu itens`() {
        // given
        var categoryModel: CategoryModel = Fixture.from(CategoryModel::class.java).gimme(
                "category pizza")
        this.categoryRepository.saveAll(listOf(categoryModel))
        categoryModel = this.categoryRepository.findByCode(categoryModel.code)!!

        val restaurantsModel: List<RestaurantModel> = Fixture.from(RestaurantModel::class.java)
                .gimme<RestaurantModel>(
                        3, "pizza hut", "braz pizzaria", "dominos pizza").map {
                    RestaurantModel(null, it.uuid, it.name, categoryModel, it.logo, it.description, it.address, it.itens)
                }
        restaurantRepository.saveAll(restaurantsModel)

        val uuidPizzaHut = UUID.fromString("cbb9c2bd-abde-48a3-891a-6229fc9b7c2f")

        // when
        val restaurant: Restaurant = restaurantGateway.findByUuid(uuidPizzaHut)

        // then
        Assertions.assertNotNull(restaurant)
        Assertions.assertNotNull(restaurant.id)
        Assertions.assertEquals("Pizza Hut", restaurant.name)
        Assertions.assertNotNull(restaurant.category.id)
        Assertions.assertNotNull(restaurant.category.uuid)
        Assertions.assertEquals("pizza", restaurant.category.code)
        Assertions.assertEquals("Pizzaria", restaurant.category.description)
        Assertions.assertEquals("https://pbs.twimg.com/profile_images/1333417326704791553/Mm0bj3oN.jpg", restaurant.logo)
        Assertions.assertEquals("Pizza Hut é uma cadeia de restaurantes e franquias especializada em pizzas e massas. Com sede na cidade de Plano, no Texas, a Pizza Hut é a maior cadeia de pizzarias do mundo, com quase 15 mil restaurantes e quiosques em mais de 130 países. Possui 95 restaurantes no Brasil e 91 em Portugal.",
                restaurant.description)
        Assertions.assertEquals("Av. Nome da avenida, 123", restaurant.address)

        // and
        Assertions.assertEquals(3, restaurant.itens.size)

        val menuItemPepperoni = restaurant.itens.find { it.uuid.toString() == "743b55f8-9543-11eb-a8b3-0242ac130003" }!!
        Assertions.assertNotNull(menuItemPepperoni.id)
        Assertions.assertEquals("Pepperoni", menuItemPepperoni.name)
        Assertions.assertEquals("Muitas fatias de pepperoni (salame especial condimentado com páprica) servidas sobre mussarela e de molho de tomate.",
                menuItemPepperoni.description)
        Assertions.assertEquals(BigDecimal("33.99"), menuItemPepperoni.value)

        val menuItemMeat = restaurant.itens.find { it.uuid.toString() == "773712b0-9543-11eb-a8b3-0242ac130003" }!!
        Assertions.assertNotNull(menuItemMeat.id)
        Assertions.assertEquals("Meat", menuItemMeat.name)
        Assertions.assertEquals("Mussarela Pizza Hut, pepperoni, presunto, carnes bovinas e suínas cobertas por bacon.",
                menuItemMeat.description)
        Assertions.assertEquals(BigDecimal("34.99"), menuItemMeat.value)

        val menuItemSupreme = restaurant.itens.find { it.uuid.toString() == "7d35de8a-9543-11eb-a8b3-0242ac130003" }!!
        Assertions.assertNotNull(menuItemSupreme.id)
        Assertions.assertEquals("Supreme", menuItemSupreme.name)
        Assertions.assertEquals("Combinação de molho de tomate, pepperoni, cebola, pimentão, champignon, seleção de carnes bovina e suína e mussarela.",
                menuItemSupreme.description)
        Assertions.assertEquals(BigDecimal("35.99"), menuItemSupreme.value)
    }
}
