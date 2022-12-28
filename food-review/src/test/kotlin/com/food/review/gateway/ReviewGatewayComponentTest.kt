package com.food.review.gateway

import com.food.review.gateway.mongo.repository.ReviewRepository
import com.food.review.lang.MongoRepositoryComponentTest
import com.food.review.lang.fixture.ReviewFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ReviewGatewayComponentTest: MongoRepositoryComponentTest() {

    @Autowired
    lateinit var reviewGateway: ReviewGateway

    @Autowired
    lateinit var reviewRepository: ReviewRepository

    @Test
    fun WhenSave_thenReviewSavedAtDatabase() {
        //given
        val reviewToSave = ReviewFixture.getValidCreated()

        //when
        val reviewSaved = this.reviewGateway.save(reviewToSave)

        //then
        val reviewFoundByIdOptional = this.reviewRepository.findById(reviewSaved.id!!)
        assertThat(reviewFoundByIdOptional).isPresent

        //and
        val reviewFoundById = reviewFoundByIdOptional.get()
        assertThat(reviewFoundById.id).isNotNull
        assertThat(reviewFoundById.uuid).isEqualTo(reviewToSave.uuid.toString())
        assertThat(reviewFoundById.status).isEqualTo(reviewToSave.status)
        assertThat(reviewFoundById.order!!.uuid).isEqualTo(reviewToSave.order.uuid.toString())
        assertThat(reviewFoundById.order!!.userUuid).isEqualTo(reviewToSave.order.userUuid.toString())
        assertThat(reviewFoundById.order!!.restaurant!!.uuid).isEqualTo(reviewToSave.order.restaurant.uuid.toString())
        assertThat(reviewFoundById.order!!.restaurant!!.name).isEqualTo(reviewToSave.order.restaurant.name)
        assertThat(reviewFoundById.order!!.total).isEqualTo(reviewToSave.order.total)
        assertThat(reviewFoundById.text).isNullOrEmpty()
        assertThat(reviewFoundById.createdAt).isNotNull
        assertThat(reviewFoundById.updatedAt).isNotNull

        //and
        assertThat(reviewFoundById.order!!.items).hasSize(reviewToSave.order.items.size)
        reviewFoundById.order!!.items!!.forEach { orderItemFound->
            val orderItemToSave = reviewToSave.order.items.find { it.uuid.toString() == orderItemFound.uuid }
            assertThat(orderItemToSave).isNotNull
            assertThat(orderItemFound.menuItemUuid).isEqualTo(orderItemToSave!!.menuItemUuid.toString())
            assertThat(orderItemFound.name).isEqualTo(orderItemToSave.name)
            assertThat(orderItemFound.amount).isEqualTo(orderItemToSave.amount)
            assertThat(orderItemFound.unitValue).isEqualTo(orderItemToSave.unitValue)
        }
    }
}
