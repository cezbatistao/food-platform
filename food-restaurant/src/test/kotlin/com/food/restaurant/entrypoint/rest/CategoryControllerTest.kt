package com.food.restaurant.entrypoint.rest

import com.food.restaurant.config.AbstractControllerIT
import com.food.restaurant.domain.Category
import com.food.restaurant.usecase.ListCategory
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(CategoryController::class)
class CategoryControllerTest: AbstractControllerIT() {

//    @Autowired
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
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0))
    }

    @Test
    fun `should return a list of categories on payload`() {
        // given
        every { listCategory.execute() } returns listOf(
                Category(1, "pizza", "Pizzaria"),
                Category(2, "hamburguer", "Hambúrguer"),
                Category(3, "vegetariana", "Vegetariana")
        ) // TODO change to fixture!

        // when
        val result: ResultActions = mockMvc.perform(
                get("/api/v1/categories"))

        // then
        Assertions.assertNotNull(result)
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(3))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].code").value("pizza"))
                .andExpect(jsonPath("$.data[0].description").value("Pizzaria"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].code").value("hamburguer"))
                .andExpect(jsonPath("$.data[1].description").value("Hambúrguer"))
                .andExpect(jsonPath("$.data[2].id").value(3))
                .andExpect(jsonPath("$.data[2].code").value("vegetariana"))
                .andExpect(jsonPath("$.data[2].description").value("Vegetariana"))
    }
}
