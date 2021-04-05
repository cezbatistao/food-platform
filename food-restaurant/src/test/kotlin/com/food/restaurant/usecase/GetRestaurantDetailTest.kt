package com.food.restaurant.usecase

import br.com.six2six.fixturefactory.Fixture
import com.food.restaurant.config.AbstractUnitTest
import com.food.restaurant.domain.MenuItem
import com.food.restaurant.domain.RestaurantDetail
import com.food.restaurant.domain.exception.EntityNotFoundException
import com.food.restaurant.gateway.RestaurantGateway
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*

class GetRestaurantDetailTest: AbstractUnitTest() {

    var restaurantGateway: RestaurantGateway = mock()

    private lateinit var getRestaurantDetail: GetRestaurantDetail

    @BeforeEach
    fun setUp() {
        Mockito.reset(restaurantGateway)

        getRestaurantDetail = GetRestaurantDetail(restaurantGateway)
    }

    @Test
    fun `should throw a entity not found exception when don't exists restaurant`() {
        // given
        val uuidRestaurant: UUID = UUID.randomUUID()

        whenever(this.restaurantGateway.findByUuid(uuidRestaurant))
                .thenThrow(EntityNotFoundException("0003", "entityNotFoundException", "Restaurant ${uuidRestaurant} don't exists"))

        // when
        val exception: EntityNotFoundException = Assertions.assertThrows(EntityNotFoundException::class.java) {
            getRestaurantDetail.execute(uuidRestaurant)
        }

        // then
        verify(restaurantGateway, times(1)).findByUuid(uuidRestaurant)

        Assertions.assertNotNull(exception)
        Assertions.assertEquals("0003", exception.code)
        Assertions.assertEquals("entityNotFoundException", exception.error)
        Assertions.assertEquals("Restaurant ${uuidRestaurant} don't exists", exception.message)
    }

    @Test
    fun `should return a restaurant with itens`() {
        // given
        val uuidRestaurant: UUID = UUID.randomUUID()

        val restaurantDetailFromGateway: RestaurantDetail = Fixture.from(RestaurantDetail::class.java).gimme("pizza hut")

        whenever(this.restaurantGateway.findByUuid(uuidRestaurant)).thenReturn(restaurantDetailFromGateway)

        // when
        val restaurantDetail: RestaurantDetail = getRestaurantDetail.execute(uuidRestaurant)

        // then
        verify(restaurantGateway, times(1)).findByUuid(uuidRestaurant)

        Assertions.assertNotNull(restaurantDetail)
        Assertions.assertEquals(restaurantDetailFromGateway.id, restaurantDetail.id)
        Assertions.assertEquals(restaurantDetailFromGateway.uuid, restaurantDetail.uuid)
        Assertions.assertEquals(restaurantDetailFromGateway.name, restaurantDetail.name)
        Assertions.assertEquals(restaurantDetailFromGateway.category, restaurantDetail.category)
        Assertions.assertEquals(restaurantDetailFromGateway.logo, restaurantDetail.logo)
        Assertions.assertEquals(restaurantDetailFromGateway.description, restaurantDetail.description)
        Assertions.assertEquals(restaurantDetailFromGateway.address, restaurantDetail.address)

        Assertions.assertEquals(restaurantDetailFromGateway.itens.size, restaurantDetail.itens.size)

        restaurantDetailFromGateway.itens.forEach {
            val menuItem: MenuItem = restaurantDetail.itens.find { menuItem: MenuItem -> menuItem.id == it.id }!!

            Assertions.assertEquals(it.name, menuItem.name)
            Assertions.assertEquals(it.uuid, menuItem.uuid)
            Assertions.assertEquals(it.description, menuItem.description)
            Assertions.assertEquals(it.value, menuItem.value)
        }
    }
}
