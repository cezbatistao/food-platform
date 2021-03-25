package com.food.restaurant.usecase

import br.com.six2six.fixturefactory.Fixture
import com.food.restaurant.config.AbstractUnitTest
import com.food.restaurant.domain.Category
import com.food.restaurant.domain.Restaurant
import com.food.restaurant.gateway.CategoryGateway
import com.food.restaurant.gateway.RestaurantGateway
import com.food.restaurant.usecase.exception.ValidationException
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class FindRestaurantsByCategoryTest: AbstractUnitTest() {

    var restaurantGateway: RestaurantGateway = mock()
    var categoryGateway: CategoryGateway = mock()

    private lateinit var findRestaurantsByCategory: FindRestaurantsByCategory

    @BeforeEach
    fun setUp() {
        Mockito.reset(restaurantGateway, categoryGateway)

        findRestaurantsByCategory = FindRestaurantsByCategory(restaurantGateway, categoryGateway)
    }

    @Test
    fun `should exception when category is null`() {
        // given
        val category: String? = null

        // when
        val exception = Assertions.assertThrows(NullPointerException::class.java) {
            findRestaurantsByCategory.execute(category!!)
        }

        // then
        Assertions.assertNotNull(exception)
    }

    @Test
    fun `should return exception when category don't exists on database`() {
        // given
        val category: Category = Fixture.from(Category::class.java).gimme(
                "category pizza")

        whenever(categoryGateway.findByCode(category.code)).thenReturn(null)

        // when
        val exception: ValidationException = Assertions.assertThrows(ValidationException::class.java) {
            findRestaurantsByCategory.execute(category.code)
        }

        // then
        Assertions.assertNotNull(exception)
        Assertions.assertEquals("1000", exception.code)
        Assertions.assertEquals("validationException", exception.error)
        Assertions.assertEquals("Category ${category.code} don't exists", exception.message)
        Assertions.assertEquals(listOf<String>(), exception.messageArgs)
    }

    @Test
    fun `should return empty list of restaurant without a category`() {
        // given
        val category: Category = Fixture.from(Category::class.java).gimme(
                "category pizza")

        whenever(categoryGateway.findByCode(category.code)).thenReturn(category)
        whenever(restaurantGateway.findAllByCategory(category)).thenReturn(listOf())

        // when
        val restaurants: List<Restaurant> = findRestaurantsByCategory.execute(category.code)

        // then
        Assertions.assertNotNull(restaurants)
        Assertions.assertEquals(0, restaurants.size)
    }

    @Test
    fun `should return a list of restaurant with category`() {
        // given
        val category: Category = Fixture.from(Category::class.java).gimme(
                "category pizza")
        val restaurantsFromGateway: List<Restaurant> = Fixture.from(Restaurant::class.java).gimme(
                3, "pizza hut", "braz pizzaria", "dominos pizza")

        whenever(categoryGateway.findByCode(category.code)).thenReturn(category)
        whenever(restaurantGateway.findAllByCategory(category)).thenReturn(restaurantsFromGateway)

        // when
        val restaurants: List<Restaurant> = findRestaurantsByCategory.execute(category.code)

        // then
        Assertions.assertNotNull(restaurants)
        Assertions.assertEquals(restaurantsFromGateway.size, restaurants.size)
        Assertions.assertEquals(restaurantsFromGateway, restaurants)
    }
}
