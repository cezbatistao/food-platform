package com.food.restaurant.entrypoint.rest

import com.food.restaurant.domain.Category
import com.food.restaurant.entrypoint.rest.json.CategoryResponse
import com.food.restaurant.entrypoint.rest.json.DataResponse
import com.food.restaurant.usecase.ListCategory
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class CategoryController(
        private val listCategory: ListCategory
) {

    @GetMapping(
            path = arrayOf("/v1/categories"),
            produces = arrayOf(APPLICATION_JSON_VALUE)
    )
    fun list(): ResponseEntity<DataResponse<List<CategoryResponse>>> {
        val categories: List<Category> = this.listCategory.execute()
        val categoriesResponse = categories.map {
            CategoryResponse(
                    it.uuid!!,
                    it.code,
                    it.description
            )
        }
        return ResponseEntity.ok().body(DataResponse(categoriesResponse));
    }
}
