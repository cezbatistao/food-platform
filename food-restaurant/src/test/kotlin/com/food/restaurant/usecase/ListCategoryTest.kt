package com.food.restaurant.usecase

import com.food.restaurant.domain.Category
import com.food.restaurant.gateway.CategoryGateway
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.reset

class ListCategoryTest {

    var categoryGateway: CategoryGateway = mock()

    private lateinit var listCategory: ListCategory

    @BeforeEach
    fun setUp() {
        reset(categoryGateway)

        listCategory = ListCategory(categoryGateway)
    }

    @Test
    fun `should return empty list when gateway don't have records`() {
        // given
        whenever(categoryGateway.list()).thenReturn(listOf())

        // when
        val categories:List<Category> = listCategory.execute()

        // then
        Assertions.assertNotNull(categories)
        Assertions.assertEquals(0, categories.size)
    }

    @Test
    fun `should return a list when gateway have records`() {
        // given
        val categoriesRecord = listOf(
                Category(1, "pizza", "Pizzaria"),
                Category(2, "hamburguer", "Hambúrguer"),
                Category(3, "vegetariana", "Vegetariana")
        )
        whenever(categoryGateway.list()).thenReturn(categoriesRecord)

        // when
        val categories:List<Category> = listCategory.execute()

        // then
        Assertions.assertNotNull(categories)
        Assertions.assertEquals(categoriesRecord.size, categories.size)
        Assertions.assertEquals(categoriesRecord, categories)
    }
}
