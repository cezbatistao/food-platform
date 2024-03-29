package com.food.restaurant.entrypoint.rest

import br.com.six2six.fixturefactory.Fixture
import com.food.restaurant.config.AbstractControllerIT
import com.food.restaurant.domain.Restaurant
import com.food.restaurant.domain.RestaurantDetail
import com.food.restaurant.domain.exception.EntityNotFoundException
import com.food.restaurant.usecase.FindRestaurantsByCategory
import com.food.restaurant.usecase.GetRestaurantDetail
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@WebMvcTest(RestaurantController::class)
class RestaurantControllerTest: AbstractControllerIT() {

    lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var findRestaurantsByCategory: FindRestaurantsByCategory

    @MockkBean
    private lateinit var getRestaurantDetail: GetRestaurantDetail

    @BeforeEach
    fun setUp() {
        mockMvc = super.buildMockMvcWithBusinessExceptionHandler()
    }

    @Test
    fun `should return a bad request when don't send a category`() {
        // given
        val category = "" // TODO verify to use where to check "   " and null values
        every {
            findRestaurantsByCategory.execute(category)
        } throws Exception("DO NOT THROW THIS EXCEPTION")

        // when
        val result: ResultActions = mockMvc.perform(
                get("/api/v1/restaurants")
                        .param("category", category))

        // then
        Assertions.assertNotNull(result)
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.code").value("0001"))
                .andExpect(jsonPath("$.error.message").value("Parameter list.category must not be blank"))
    }

    @Test
    fun `should return a not found when category don't exists`() {
        // given
        val category = "pizza"
        every {
            findRestaurantsByCategory.execute(category)
        } throws EntityNotFoundException("0002", "entityNotFoundException", "Category ${category} don't exists")

        // when
        val result: ResultActions = mockMvc.perform(
                get("/api/v1/restaurants")
                        .param("category", category))

        // then
        Assertions.assertNotNull(result)
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.code").value("0002"))
                .andExpect(jsonPath("$.error.message").value("Category ${category} don't exists"))
    }

    @Test
    fun `should return a empty list with no restaurants from a category`() {
        // given
        val category = "pizza"
        every { findRestaurantsByCategory.execute(category) } returns listOf()

        // when
        val result: ResultActions = mockMvc.perform(
                get("/api/v1/restaurants")
                        .param("category", category))

        // then
        Assertions.assertNotNull(result)
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0))
    }

    @Test
    fun `should return a list of restaurants from a category`() {
        // given
        val category = "pizza"

        val restaurants: List<Restaurant> = Fixture.from(Restaurant::class.java).gimme(
                3, "pizza hut", "braz pizzaria", "dominos pizza")

        every { findRestaurantsByCategory.execute(category) } returns restaurants

        // when
        val result: ResultActions = mockMvc.perform(
                get("/api/v1/restaurants")
                        .param("category", category))

        // then
        Assertions.assertNotNull(result)
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(3))
                .andExpect(jsonPath("$.data[0].uuid").value("cbb9c2bd-abde-48a3-891a-6229fc9b7c2f"))
                .andExpect(jsonPath("$.data[0].name").value("Pizza Hut"))
                .andExpect(jsonPath("$.data[0].category.uuid").value("0eda194c-827c-4254-ada8-214115310fc6"))
                .andExpect(jsonPath("$.data[0].category.code").value("pizza"))
                .andExpect(jsonPath("$.data[0].category.description").value("Pizzaria"))
                .andExpect(jsonPath("$.data[0].logo").value("https://pbs.twimg.com/profile_images/1333417326704791553/Mm0bj3oN.jpg"))
                .andExpect(jsonPath("$.data[0].description").value("Pizza Hut é uma cadeia de restaurantes e franquias especializada em pizzas e massas. Com sede na cidade de Plano, no Texas, a Pizza Hut é a maior cadeia de pizzarias do mundo, com quase 15 mil restaurantes e quiosques em mais de 130 países. Possui 95 restaurantes no Brasil e 91 em Portugal."))
                .andExpect(jsonPath("$.data[0].address").value("Av. Nome da avenida, 123"))
                .andExpect(jsonPath("$.data[1].uuid").value("23e0211c-19ea-47ee-b98e-77e023e1a95f"))
                .andExpect(jsonPath("$.data[1].name").value("Bráz Pizzaria"))
                .andExpect(jsonPath("$.data[1].category.uuid").value("0eda194c-827c-4254-ada8-214115310fc6"))
                .andExpect(jsonPath("$.data[1].category.code").value("pizza"))
                .andExpect(jsonPath("$.data[1].category.description").value("Pizzaria"))
                .andExpect(jsonPath("$.data[1].logo").value("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSwwg9Nscz6dkUtIpPUB0u1tAxfZmJcmcfGTA&usqp=CAU"))
                .andExpect(jsonPath("$.data[1].description").value("Pizzaria reúne ingredientes clássicos e sofisticados e muito burburinho e aconchego frente aos fornos à lenha."))
                .andExpect(jsonPath("$.data[1].address").value("Av. Nome da outra avenida, 789"))
                .andExpect(jsonPath("$.data[2].uuid").value("36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c"))
                .andExpect(jsonPath("$.data[2].name").value("Domino's Pizza"))
                .andExpect(jsonPath("$.data[2].category.uuid").value("0eda194c-827c-4254-ada8-214115310fc6"))
                .andExpect(jsonPath("$.data[2].category.code").value("pizza"))
                .andExpect(jsonPath("$.data[2].category.description").value("Pizzaria"))
                .andExpect(jsonPath("$.data[2].logo").value("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQd7Ow6kgZoZk4h4oybgOkRJf6LZ_NLpBhfRA&usqp=CAU"))
                .andExpect(jsonPath("$.data[2].description").value("Domino's Pizza é uma empresa de alimentação baseada em pizzas. Atualmente, é a maior rede de entregas de pizzas do mundo, com 13.000 lojas em 83 países"))
                .andExpect(jsonPath("$.data[2].address").value("Rua Nome da rua, 654"))
    }

    @Test
    fun `should return error payload when have internal server error`() {
        // given
        val category = "pizza"

        every {
            findRestaurantsByCategory.execute(category)
        } throws Exception("Error on this test")

        // when
        val result: ResultActions = mockMvc.perform(
                get("/api/v1/restaurants")
                        .param("category", category))

        // then
        Assertions.assertNotNull(result)
        result.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.code").value("9999"))
                .andExpect(jsonPath("$.error.message").value("Error on this test"))
    }

    @Test
    fun `should return a bad request with invalid uuid`() {
        // given
        val uuidRestaurant = "w453f23f3223"

        val restaurantDetail: RestaurantDetail = Fixture.from(RestaurantDetail::class.java).gimme(
                "dominos pizza")

        every { getRestaurantDetail.execute(UUID.fromString("36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c")) } returns restaurantDetail

        // when
        val result: ResultActions = mockMvc.perform(
                get("/api/v1/restaurants/{uuid}", uuidRestaurant))

        // then
        Assertions.assertNotNull(result)
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.code").value("0001"))
                .andExpect(jsonPath("$.error.message").value("Parameter getOne.uuid must match \"^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}\$\""))
    }

    @Test
    fun `should return a not found payload message`() {
        // given
        val uuidRestaurant = "36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c"

        val restaurantDetail: RestaurantDetail = Fixture.from(RestaurantDetail::class.java).gimme(
                "dominos pizza")

        every {
            getRestaurantDetail.execute(UUID.fromString(uuidRestaurant))
        } throws EntityNotFoundException("0003", "entityNotFoundException", "Restaurant ${uuidRestaurant} don't exists")

        // when
        val result: ResultActions = mockMvc.perform(
                get("/api/v1/restaurants/{uuid}", uuidRestaurant))

        // then
        Assertions.assertNotNull(result)
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.code").value("0003"))
                .andExpect(jsonPath("$.error.message").value("Restaurant ${uuidRestaurant} don't exists"))
    }

    @Test
    fun `should get a restaurant detail with menu itens`() {
        // given
        val uuidRestaurant = "36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c"

        val restaurantDetail: RestaurantDetail = Fixture.from(RestaurantDetail::class.java).gimme(
                "dominos pizza")

        every { getRestaurantDetail.execute(UUID.fromString(uuidRestaurant)) } returns restaurantDetail

        // when
        val result: ResultActions = mockMvc.perform(
                get("/api/v1/restaurants/{uuid}", uuidRestaurant))

        // then
        Assertions.assertNotNull(result)
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.uuid").value("36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c"))
                .andExpect(jsonPath("$.data.name").value("Domino's Pizza"))
                .andExpect(jsonPath("$.data.category.uuid").value("0eda194c-827c-4254-ada8-214115310fc6"))
                .andExpect(jsonPath("$.data.category.code").value("pizza"))
                .andExpect(jsonPath("$.data.category.description").value("Pizzaria"))
                .andExpect(jsonPath("$.data.logo").value("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQd7Ow6kgZoZk4h4oybgOkRJf6LZ_NLpBhfRA&usqp=CAU"))
                .andExpect(jsonPath("$.data.description").value("Domino's Pizza é uma empresa de alimentação baseada em pizzas. Atualmente, é a maior rede de entregas de pizzas do mundo, com 13.000 lojas em 83 países"))
                .andExpect(jsonPath("$.data.address").value("Rua Nome da rua, 654"))
                .andExpect(jsonPath("$.data.itens").isArray())
                .andExpect(jsonPath("$.data.itens.length()").value(2))
                .andExpect(jsonPath("$.data.itens[0].uuid").value("843bfe62-9543-11eb-a8b3-0242ac130003"))
                .andExpect(jsonPath("$.data.itens[0].name").value("Pepperoni"))
                .andExpect(jsonPath("$.data.itens[0].description").value("Mussarela, oregano e pepperoni."))
                .andExpect(jsonPath("$.data.itens[0].value").value("33.99"))
                .andExpect(jsonPath("$.data.itens[1].uuid").value("88e3812e-9543-11eb-a8b3-0242ac130003"))
                .andExpect(jsonPath("$.data.itens[1].name").value("Mussarela"))
                .andExpect(jsonPath("$.data.itens[1].description").value("Queijo mussarela e oregano."))
                .andExpect(jsonPath("$.data.itens[1].value").value("31.99"))
    }
}