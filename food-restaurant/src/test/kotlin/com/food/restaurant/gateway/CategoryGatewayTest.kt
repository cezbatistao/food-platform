package com.food.restaurant.gateway

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
        val categoriesModel = listOf(
                CategoryModel("pizza", "Pizzaria"),
                CategoryModel("hamburguer", "Hambúrguer"),
                CategoryModel("vegetariana", "Vegetariana")
        ) // TODO change to fixture!
        this.categoryRepository.saveAll(categoriesModel)

        // when
        val categories: List<Category> = categoryGateway.list()

        // then
        Assertions.assertNotNull(categories)
        Assertions.assertEquals(categoriesModel.size, categories.size)

        categoriesModel.forEach { categoryModel ->
            val category: Category = categories.find { it.code == categoryModel.code }!!
            Assertions.assertNotNull(category.id)
            Assertions.assertEquals(categoryModel.description, category.description)
        }
    }
}
