package com.food.review.usecase

import com.food.review.domain.Review
import com.food.review.gateway.ReviewGateway
import com.food.review.gateway.ReviewSendGateway
import com.food.review.lang.fixture.OrderFixture
import com.food.review.lang.fixture.ReviewFixture
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CreateReviewTest {

    val reviewGateway: ReviewGateway = mockk()
    val reviewSendGateway: ReviewSendGateway = mockk()

    val createReview = CreateReview(reviewGateway, reviewSendGateway)

    @Test
    fun whenExecuteCreateReview_thenVerifyReviewWasCreated() {
        //given
        val order = OrderFixture.getValid()
        val reviewSaved = ReviewFixture.getValidCreatedSaved()

        val reviewToSaveSlot = slot<Review>()

        every { reviewGateway.save(capture(reviewToSaveSlot)) } returns reviewSaved
        justRun { reviewSendGateway.sendCreated(any()) }

        //when
        createReview.execute(order)

        //then
        verify(exactly = 1) { reviewGateway.save(any()) }
        verify(exactly = 1) { reviewSendGateway.sendCreated(any()) }

        //and
        val reviewCaptured = reviewToSaveSlot.captured
        assertThat(reviewCaptured.id).isNull()
        assertThat(reviewCaptured.uuid).isNotNull
        assertThat(reviewCaptured.status).isEqualTo(reviewSaved.status)
        assertThat(reviewCaptured.restaurant).isEqualTo(reviewSaved.restaurant)
        assertThat(reviewCaptured.orderUuid).isEqualTo(reviewSaved.orderUuid)
        assertThat(reviewCaptured.userUuid).isEqualTo(reviewSaved.userUuid)
        assertThat(reviewCaptured.items).isEqualTo(reviewSaved.items)
        assertThat(reviewCaptured.text).isNullOrEmpty()
        assertThat(reviewCaptured.createdAt).isNull()
        assertThat(reviewCaptured.updatedAt).isNull()
    }
}
