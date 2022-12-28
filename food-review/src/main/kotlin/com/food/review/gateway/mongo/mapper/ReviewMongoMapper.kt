package com.food.review.gateway.mongo.mapper

import com.food.review.domain.Review
import com.food.review.gateway.mongo.entity.ReviewMongo
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface ReviewMongoMapper {

    companion object {
        var INSTANCE = Mappers.getMapper(ReviewMongoMapper::class.java)
    }

    fun map(review: Review): ReviewMongo

    fun map(reviewMongo: ReviewMongo): Review

}
