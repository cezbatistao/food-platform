package com.food.restaurant.entrypoint.rest

import com.food.restaurant.domain.Restaurant
import com.food.restaurant.domain.RestaurantDetail
import com.food.restaurant.entrypoint.rest.json.*
import com.food.restaurant.usecase.FindRestaurantsByCategory
import com.food.restaurant.usecase.GetRestaurantDetail
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.constraints.NotBlank

@Validated
@RestController
@RequestMapping("/api")
class RestaurantController(
        private val findRestaurantsByCategory: FindRestaurantsByCategory,
        private val getRestaurantDetail: GetRestaurantDetail
) {

    @GetMapping(
            path = arrayOf("/v1/restaurants"),
            produces = arrayOf(APPLICATION_JSON_VALUE)
    )
    fun list(
            @RequestParam(name = "category", required = true) @NotBlank category: String
    ): ResponseEntity<DataResponse<List<RestaurantResponse>>> {

        val restaurants: List<Restaurant> = this.findRestaurantsByCategory.execute(category)

        val restaurantResponse = restaurants.map {
            RestaurantResponse(
                    it.uuid!!,
                    it.name,
                    CategoryResponse(
                            it.category.uuid!!,
                            it.category.code,
                            it.category.description
                    ),
                    it.logo,
                    it.description,
                    it.address
            )
        }
        return ResponseEntity.ok().body(DataResponse(restaurantResponse));
    }

    @GetMapping(
            path = arrayOf("/v1/restaurants/{uuid}"),
            produces = arrayOf(APPLICATION_JSON_VALUE)
    )
    fun getOne(
            @PathVariable(name = "uuid") @NotBlank @Uuid uuid: String
    ): ResponseEntity<DataResponse<RestaurantDetailResponse>> {

        val restaurantDetail: RestaurantDetail = this.getRestaurantDetail.execute(UUID.fromString(uuid))

        val restaurantDetailResponse = RestaurantDetailResponse(
                restaurantDetail.uuid!!,
                restaurantDetail.name,
                CategoryResponse(
                        restaurantDetail.category.uuid!!,
                        restaurantDetail.category.code,
                        restaurantDetail.category.description
                ),
                restaurantDetail.logo,
                restaurantDetail.description,
                restaurantDetail.address,
                restaurantDetail.itens.map {
                    MenuItemResponse(it.uuid!!, it.name, it.description, it.value.toString())
                }
        )

        return ResponseEntity.ok().body(DataResponse(restaurantDetailResponse));
    }
}
