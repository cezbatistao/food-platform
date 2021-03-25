package com.food.restaurant.entrypoint.rest

import br.com.six2six.fixturefactory.Fixture
import com.food.restaurant.config.AbstractControllerIT
import com.food.restaurant.domain.Category
import com.food.restaurant.usecase.ListCategory
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

@WebMvcTest(CategoryController::class)
class CategoryControllerTest: AbstractControllerIT() {

    lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var listCategory: ListCategory

    @BeforeEach
    fun setUp() {
        mockMvc = super.buildMockMvcWithBusinessExceptionHandler()
    }

    @Test
    fun `should return empty payload with no records`() {
        // given
        every { listCategory.execute() } returns listOf()

        // when
        val result: ResultActions = mockMvc.perform(
                get("/api/v1/categories"))

        // then
        Assertions.assertNotNull(result)
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0))
    }

    @Test
    fun `should return a list of categories on payload`() {
        // given
        val categories: List<Category> = Fixture.from(Category::class.java).gimme(
                3,"category pizza", "category hamburguer", "category vegetariana")

        every { listCategory.execute() } returns categories

        // when
        val result: ResultActions = mockMvc.perform(
                get("/api/v1/categories"))

        // then
        Assertions.assertNotNull(result)
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(3))
                .andExpect(jsonPath("$.data[0].uuid").value("0eda194c-827c-4254-ada8-214115310fc6"))
                .andExpect(jsonPath("$.data[0].code").value("pizza"))
                .andExpect(jsonPath("$.data[0].description").value("Pizzaria"))
                .andExpect(jsonPath("$.data[1].uuid").value("cc0cd1b9-4217-498d-8da8-661de03b86e5"))
                .andExpect(jsonPath("$.data[1].code").value("hamburguer"))
                .andExpect(jsonPath("$.data[1].description").value("Hambúrguer"))
                .andExpect(jsonPath("$.data[2].uuid").value("67e92973-f1ff-4cc3-b2c0-6485939ae442"))
                .andExpect(jsonPath("$.data[2].code").value("vegetariana"))
                .andExpect(jsonPath("$.data[2].description").value("Vegetariana"))
    }

    @Test
    fun `should return error payload when have internal server error`() {
        // given
        every { listCategory.execute() } throws Exception("Error on this test")

        // when
        val result: ResultActions = mockMvc.perform(
                get("/api/v1/categories"))

        // then
        Assertions.assertNotNull(result)
        result.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.code").value("9999"))
                .andExpect(jsonPath("$.error.message").value("Error on this test"))
    }
}
