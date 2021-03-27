package com.food.restaurant.gateway.database

import com.food.restaurant.domain.Category
import com.food.restaurant.gateway.CategoryGateway
import com.food.restaurant.gateway.database.model.CategoryModel
import com.food.restaurant.gateway.database.repository.CategoryRepository
import org.springframework.stereotype.Component

@Component
class CategoryGatewayImpl(
        private val categoryRepository: CategoryRepository
): CategoryGateway {

    override fun list(): List<Category> {
        val listAll: List<CategoryModel> = categoryRepository.listAll()

        return listAll.map { categoryModel -> Category(
                categoryModel.id, categoryModel.code, categoryModel.description
        ) }
    }
}