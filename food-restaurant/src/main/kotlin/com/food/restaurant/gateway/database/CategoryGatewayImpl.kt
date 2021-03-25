package com.food.restaurant.gateway.database

import com.food.restaurant.domain.Category
import com.food.restaurant.domain.exception.EntityNotFoundException
import com.food.restaurant.gateway.CategoryGateway
import com.food.restaurant.gateway.database.model.CategoryModel
import com.food.restaurant.gateway.database.repository.CategoryRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class CategoryGatewayImpl(
        private val categoryRepository: CategoryRepository
): CategoryGateway {

    override fun findByCode(category: String): Category {
        val categoryModel: CategoryModel = (categoryRepository.findByCode(category)
                ?: throw EntityNotFoundException("0002", "entityNotFoundException", "Category ${category} don't exists"))

        return Category(categoryModel.id!!, UUID.fromString(categoryModel.uuid), categoryModel.code, categoryModel.description)
    }

    override fun list(): List<Category> {
        val listAll: List<CategoryModel> = categoryRepository.listAll()

        return listAll.map { categoryModel -> Category(
                categoryModel.id, UUID.fromString(categoryModel.uuid), categoryModel.code, categoryModel.description
        ) }
    }
}
