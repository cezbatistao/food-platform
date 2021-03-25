package com.food.restaurant.usecase

import br.com.six2six.fixturefactory.Fixture
import com.food.restaurant.config.AbstractUnitTest
import com.food.restaurant.domain.Category
import com.food.restaurant.gateway.CategoryGateway
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.reset

class ListCategoryTest: AbstractUnitTest() {

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
        val categoriesMock: List<Category> = Fixture.from(Category::class.java).gimme(
                3,"category pizza", "category hamburguer", "category vegetariana")
        whenever(categoryGateway.list()).thenReturn(categoriesMock)

        // when
        val categories: List<Category> = listCategory.execute()

        // then
        Assertions.assertNotNull(categories)
        Assertions.assertEquals(categoriesMock.size, categories.size)
        Assertions.assertEquals(categoriesMock, categories)
    }
}
