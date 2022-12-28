package com.food.review.config.mongock.migration

import com.food.review.gateway.mongo.entity.ReviewMongo
import io.mongock.api.annotations.ChangeUnit
import io.mongock.api.annotations.Execution
import io.mongock.api.annotations.RollbackExecution
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.index.Index

@ChangeUnit(id="create-reviews-indexes", order = "1", author = "cezbatistao")
class CreateReviewsIndexes {

    @Execution
    fun changeSet(mongoTemplate: MongoTemplate) {
        arrayListOf(
            Index().on("uuid", Direction.ASC),
            Index().on("userUuid", Direction.ASC).on("createdAt", Direction.DESC),
            Index().on("status", Direction.ASC),
            Index().on("restaurant.uuid", Direction.ASC),
        ).forEach { textIndexDefinition ->
            mongoTemplate.indexOps(ReviewMongo::class.java).ensureIndex(textIndexDefinition)
        }
    }

    @RollbackExecution
    fun rollback() {
        // nothing to do
    }
}
