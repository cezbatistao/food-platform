package com.food.review.usecase

import com.food.review.domain.OrderItem
import com.food.review.domain.Review
import com.food.review.domain.ReviewPageable
import com.food.review.domain.ReviewStatus.CREATED
import com.food.review.gateway.ReviewGateway
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.instancio.Instancio
import org.instancio.Select
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl

class FindReviewsByUserUuidTest {

    val reviewGateway: ReviewGateway = mockk()

    val findReviewsByUserUuid = FindReviewsByUserUuid(reviewGateway)

    @Test
    fun whenExecuteFindReviewFromUserUuidByUuidDoesntExists_thenVerifyEntityNotFoundException() {
        //given
        val userUuid = UUID.fromString("dbb9c2bd-abde-48a3-891a-6229fc9b7c21")
        val uuid = UUID.fromString("f58ceb5b-6e70-489c-907b-0efe3b2feca5")

        val reviewCreated = Instancio.of(Review::class.java)
            .set(Select.field("uuid"), userUuid)
            .set(Select.field("text"), "")
            .set(Select.field("status"), CREATED)
            .set(Select.field("userUuid"), uuid)
            .generate(Select.field("items")) { gen -> gen.collection<OrderItem>().minSize(1).maxSize(3) }
            .create()

        val slotReviewPageable = slot<ReviewPageable>()
        every { reviewGateway.findByUserUuid(capture(slotReviewPageable)) } returns PageImpl(arrayListOf(reviewCreated))

        val reviewPageable = ReviewPageable(userUuid, reviewCreated.restaurant.uuid, reviewCreated.status, 0, 20)

        //then
        val reviewPage = findReviewsByUserUuid.execute(reviewPageable)

        //then
        verify(exactly = 1) { reviewGateway.findByUserUuid(any()) }

        val reviewPageableCapture = slotReviewPageable.captured
        assertThat(reviewPageableCapture.userUuid).isEqualTo(userUuid)
        assertThat(reviewPageableCapture.restaurantUuid).isEqualTo(reviewPageable.restaurantUuid)
        assertThat(reviewPageableCapture.status).isEqualTo(reviewPageable.status)
        assertThat(reviewPageableCapture.size).isEqualTo(reviewPageable.size)
        assertThat(reviewPageableCapture.page).isEqualTo(reviewPageable.page)

        //and
        assertThat(reviewPage.number).isEqualTo(0)
        assertThat(reviewPage.size).isEqualTo(1)
        assertThat(reviewPage.totalElements).isEqualTo(1)
        assertThat(reviewPage.totalPages).isEqualTo(1)
        assertThat(reviewPage.content).contains(reviewCreated)
    }
}