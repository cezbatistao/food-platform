package com.food.review.entrypoint.rest.mapper

import com.food.review.domain.Review
import com.food.review.entrypoint.rest.json.response.ReviewResponse
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface ReviewResponseMapper {

    companion object {
        var INSTANCE = Mappers.getMapper(ReviewResponseMapper::class.java)
    }

    fun map(reviews: List<Review>): List<ReviewResponse>

    fun map(review: Review): ReviewResponse

}