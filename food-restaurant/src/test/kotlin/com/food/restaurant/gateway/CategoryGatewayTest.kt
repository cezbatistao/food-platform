package com.food.restaurant.gateway

import br.com.six2six.fixturefactory.Fixture
import com.food.restaurant.config.AbstractRepositoryIT
import com.food.restaurant.domain.Category
import com.food.restaurant.gateway.database.model.CategoryModel
import com.food.restaurant.gateway.database.repository.CategoryRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class CategoryGatewayTest: AbstractRepositoryIT() {

    @Autowired
    lateinit var categoryGateway: CategoryGateway

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @Test
    fun `should return empty list when database is empty`() {
        // when
        val categories: List<Category> = categoryGateway.list()

        // then
        Assertions.assertNotNull(categories)
        Assertions.assertEquals(0, categories.size)
    }

    @Test
    fun `should return a list of categories`() {
        // given
        val categoriesMock: List<CategoryModel> = Fixture.from(CategoryModel::class.java).gimme(
                3,"category pizza", "category hamburguer", "category vegetariana")
        this.categoryRepository.saveAll(categoriesMock)

        // when
        val categories: List<Category> = categoryGateway.list()

        // then
        Assertions.assertNotNull(categories)
        Assertions.assertEquals(categoriesMock.size, categories.size)

        categoriesMock.forEach { categoryModel ->
            val category: Category = categories.find { it.code == categoryModel.code }!!
            Assertions.assertNotNull(category.id)
            Assertions.assertEquals(categoryModel.description, category.description)
        }
    }
}
