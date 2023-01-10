package com.food.review.usecase

import com.food.review.domain.exception.EntityNotFoundException
import com.food.review.domain.Commentary
import com.food.review.domain.OrderItem
import com.food.review.domain.Review
import com.food.review.domain.ReviewStatus.CREATED
import com.food.review.domain.ReviewStatus.VERIFIED
import com.food.review.gateway.ReviewGateway
import com.food.review.gateway.ReviewSendGateway
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import java.util.UUID
import org.assertj.core.api.Assertions
import org.instancio.Instancio
import org.instancio.Select
import org.junit.jupiter.api.Test

class AddCommentaryAtReviewTest {

    val reviewGateway: ReviewGateway = mockk()
    val reviewSendGateway: ReviewSendGateway = mockk()

    val addCommentaryAtReview = AddCommentaryAtReview(reviewGateway, reviewSendGateway)

    @Test
    fun whenExecuteAddCommentaryAtReviewDoesntExists_thenVerifyEntityNotFoundException() {
        val userUuid = UUID.fromString("dbb9c2bd-abde-48a3-891a-6229fc9b7c21")
        val uuid = UUID.fromString("f58ceb5b-6e70-489c-907b-0efe3b2feca5")

        every { reviewGateway.findByUserUuidAndUuid(userUuid, uuid) } throws
                EntityNotFoundException("0001", "entityNotFoundException", "Review doesn't exists")

        val commentary = Commentary(userUuid, uuid, "Good food!")

        //when
        val thrown = Assertions.catchThrowableOfType(
            { addCommentaryAtReview.execute(commentary) },
            EntityNotFoundException::class.java
        )

        //then
        verify(exactly = 1) { reviewGateway.findByUserUuidAndUuid(userUuid, uuid) }
        verify(exactly = 0) { reviewGateway.save(any()) }
        verify(exactly = 0) { reviewSendGateway.sendVerified(any()) }

        //and
        Assertions.assertThat(thrown).isInstanceOf(EntityNotFoundException::class.java)
            .hasMessageContaining("Review doesn't exists")
        Assertions.assertThat(thrown.code).isEqualTo("0001")
        Assertions.assertThat(thrown.error).isEqualTo("entityNotFoundException")
    }

    @Test
    fun whenExecuteAddCommentary_thenVerifyReviewWasUpdated() {
        val userUuid = UUID.fromString("dbb9c2bd-abde-48a3-891a-6229fc9b7c21")
        val uuid = UUID.fromString("f58ceb5b-6e70-489c-907b-0efe3b2feca5")

        val reviewCreated = Instancio.of(Review::class.java)
            .set(Select.field("uuid"), userUuid)
            .set(Select.field("text"), "")
            .set(Select.field("status"), CREATED)
            .set(Select.field("userUuid"), uuid)
            .generate(Select.field("items")) { gen -> gen.collection<OrderItem>().minSize(1).maxSize(3) }
            .create()

        every { reviewGateway.findByUserUuidAndUuid(userUuid, uuid) } returns reviewCreated

        val reviewToSaveSlot = slot<Review>()
        every { reviewGateway.save(capture(reviewToSaveSlot)) } returns reviewCreated

        justRun { reviewSendGateway.sendVerified(any()) }

        val commentary = Commentary(userUuid, uuid, "Good food!")

        //when
        addCommentaryAtReview.execute(commentary)

        //then
        verify(exactly = 1) { reviewGateway.findByUserUuidAndUuid(userUuid, uuid) }
        verify(exactly = 1) { reviewGateway.save(any()) }
        verify(exactly = 1) { reviewSendGateway.sendVerified(any()) }

        //and
        val reviewCaptured = reviewToSaveSlot.captured
        Assertions.assertThat(reviewCaptured.id).isEqualTo(reviewCreated.id)
        Assertions.assertThat(reviewCaptured.uuid).isEqualTo(reviewCreated.uuid)
        Assertions.assertThat(reviewCaptured.status).isEqualTo(VERIFIED)
        Assertions.assertThat(reviewCaptured.restaurant).isEqualTo(reviewCreated.restaurant)
        Assertions.assertThat(reviewCaptured.orderUuid).isEqualTo(reviewCreated.orderUuid)
        Assertions.assertThat(reviewCaptured.userUuid).isEqualTo(reviewCreated.userUuid)
        Assertions.assertThat(reviewCaptured.items).isEqualTo(reviewCreated.items)
        Assertions.assertThat(reviewCaptured.text).isEqualTo(commentary.text)
        Assertions.assertThat(reviewCaptured.createdAt).isNotNull
        Assertions.assertThat(reviewCaptured.updatedAt).isNotNull
    }
}