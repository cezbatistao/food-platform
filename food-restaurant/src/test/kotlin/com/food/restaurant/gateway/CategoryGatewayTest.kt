package com.food.restaurant.gateway

import br.com.six2six.fixturefactory.Fixture
import com.food.restaurant.config.AbstractRepositoryIT
import com.food.restaurant.domain.Category
import com.food.restaurant.domain.exception.EntityNotFoundException
import com.food.restaurant.gateway.database.model.CategoryModel
import com.food.restaurant.gateway.database.repository.CategoryRepository
import com.food.restaurant.usecase.exception.ValidationException
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

    @Test
    fun `should throw exception entity not found when don't exist category`() {
        // given
        val category = "pizza"

        // when
        val exception: EntityNotFoundException = Assertions.assertThrows(EntityNotFoundException::class.java) {
            categoryGateway.findByCode(category)
        }

        // then
        Assertions.assertNotNull(exception)
        Assertions.assertEquals("0002", exception.code)
        Assertions.assertEquals("entityNotFoundException", exception.error)
        Assertions.assertEquals("Category ${category} don't exists", exception.message)
    }

    @Test
    fun `should return a category by code`() {
        // given
        val categoryModel: CategoryModel = Fixture.from(CategoryModel::class.java).gimme(
                "category pizza")
        this.categoryRepository.saveAll(listOf(categoryModel))

        // when
        val category: Category = categoryGateway.findByCode(categoryModel.code)

        // then
        Assertions.assertNotNull(category)
        Assertions.assertNotNull(category.id)
        Assertions.assertEquals("pizza", category.code)
        Assertions.assertEquals("Pizzaria", category.description)
    }
}
