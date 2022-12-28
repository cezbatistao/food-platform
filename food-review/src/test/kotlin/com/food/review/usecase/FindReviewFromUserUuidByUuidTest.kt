package com.food.review.usecase

import com.food.restaurant.domain.exception.EntityNotFoundException
import com.food.review.domain.Commentary
import com.food.review.domain.OrderItem
import com.food.review.domain.Review
import com.food.review.domain.ReviewStatus.CREATED
import com.food.review.gateway.ReviewGateway
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import java.util.UUID
import org.assertj.core.api.Assertions
import org.instancio.Instancio
import org.instancio.Select
import org.junit.jupiter.api.Test

class FindReviewFromUserUuidByUuidTest {

    val reviewGateway: ReviewGateway = mockk()

    val findReviewFromUserUuidByUuid = FindReviewFromUserUuidByUuid(reviewGateway)

    @Test
    fun whenExecuteFindReviewFromUserUuidByUuidDoesntExists_thenVerifyEntityNotFoundException() {
        val userUuid = UUID.fromString("dbb9c2bd-abde-48a3-891a-6229fc9b7c21")
        val uuid = UUID.fromString("f58ceb5b-6e70-489c-907b-0efe3b2feca5")

        every { reviewGateway.findByUserUuidAndUuid(userUuid, uuid) } throws
                EntityNotFoundException("0001", "entityNotFoundException", "Review doesn't exists")

        //when
        val thrown = Assertions.catchThrowableOfType(
            { findReviewFromUserUuidByUuid.execute(userUuid, uuid) },
            EntityNotFoundException::class.java
        )

        //then
        verify(exactly = 1) { reviewGateway.findByUserUuidAndUuid(userUuid, uuid) }

        //and
        Assertions.assertThat(thrown).isInstanceOf(EntityNotFoundException::class.java)
            .hasMessageContaining("Review doesn't exists")
        Assertions.assertThat(thrown.code).isEqualTo("0001")
        Assertions.assertThat(thrown.error).isEqualTo("entityNotFoundException")
    }

    @Test
    fun whenExecuteFindReviewFromUserUuidByUuid_thenVerifyAndReturnReview() {
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

        //when
        val reviewReturned = findReviewFromUserUuidByUuid.execute(userUuid, uuid)

        //then
        verify(exactly = 1) { reviewGateway.findByUserUuidAndUuid(userUuid, uuid) }

        //and
        Assertions.assertThat(reviewReturned.id).isEqualTo(reviewCreated.id)
        Assertions.assertThat(reviewReturned.uuid).isEqualTo(reviewCreated.uuid)
        Assertions.assertThat(reviewReturned.status).isEqualTo(reviewCreated.status)
        Assertions.assertThat(reviewReturned.restaurant).isEqualTo(reviewCreated.restaurant)
        Assertions.assertThat(reviewReturned.orderUuid).isEqualTo(reviewCreated.orderUuid)
        Assertions.assertThat(reviewReturned.userUuid).isEqualTo(reviewCreated.userUuid)
        Assertions.assertThat(reviewReturned.items).isEqualTo(reviewCreated.items)
        Assertions.assertThat(reviewReturned.text).isEqualTo(reviewCreated.text)
        Assertions.assertThat(reviewReturned.createdAt).isNotNull
        Assertions.assertThat(reviewReturned.updatedAt).isNotNull
    }
}
