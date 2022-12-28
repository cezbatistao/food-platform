package com.food.review.entrypoint.rest

import com.food.restaurant.domain.exception.EntityNotFoundException
import com.food.review.domain.Commentary
import com.food.review.domain.Review
import com.food.review.domain.ReviewPageable
import com.food.review.domain.ReviewStatus
import com.food.review.entrypoint.rest.json.request.ReviewRequest
import com.food.review.lang.ControllerComponentTest
import com.food.review.usecase.AddCommentaryAtReview
import com.food.review.usecase.FindReviewFromUserUuidByUuid
import com.food.review.usecase.FindReviewsByUserUuid
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import io.mockk.slot
import io.mockk.verify
import java.util.UUID
import java.util.stream.Collectors
import org.assertj.core.api.Assertions.assertThat
import org.instancio.Instancio
import org.instancio.Select.field
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


@WebMvcTest(ReviewController::class)
class ReviewControllerTest: ControllerComponentTest() {

    lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var findReviewsByUserUuid: FindReviewsByUserUuid

    @MockkBean
    private lateinit var findReviewFromUserUuidByUuid: FindReviewFromUserUuidByUuid

    @MockkBean
    private lateinit var addCommentaryAtReview: AddCommentaryAtReview

    @BeforeEach
    fun setUp() {
        mockMvc = super.buildMockMvcWithBusinessExceptionHandler()
    }

    @Test
    fun whenExecuteFindReviewsWithoutResult_thenReturnOkdAndEmptyResult() {
        // given
        val userUuid = "dbb9c2bd-abde-48a3-891a-6229fc9b7c21"

        val reviewPageableSlot = slot<ReviewPageable>()
        every { findReviewsByUserUuid.execute(capture(reviewPageableSlot)) } returns Page.empty()

        // when
        val result: ResultActions = mockMvc.perform(
            get("/api/v1/{userUuid}/reviews", userUuid)
        )

        // then
        verify(exactly = 1) { findReviewsByUserUuid.execute(any()) }

        assertThat(result).isNotNull
        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").doesNotExist())
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.page").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total_pages").value(1))

        // and
        val reviewPageableCaptured = reviewPageableSlot.captured
        assertThat(reviewPageableCaptured).isNotNull
        assertThat(reviewPageableCaptured.userUuid).isEqualTo(UUID.fromString(userUuid))
        assertThat(reviewPageableCaptured.restaurantUuid).isNull()
        assertThat(reviewPageableCaptured.status).isNull()
        assertThat(reviewPageableCaptured.page).isEqualTo(0)
        assertThat(reviewPageableCaptured.size).isEqualTo(10)
    }

    @Test
    fun whenExecuteFindReviewsWithoutResultAndPageableNewValues_thenReturnOkAndVerifyPageableAndPageReturn() {
        // given
        val userUuid = "dbb9c2bd-abde-48a3-891a-6229fc9b7c21"
        val restaurantUuid = "36159a9b-f4d0-4f52-8d0f-3cd0dc702c1c"

        val reviewPageableSlot = slot<ReviewPageable>()
        every { findReviewsByUserUuid.execute(capture(reviewPageableSlot)) } returns Page.empty()

        // when
        val result: ResultActions = mockMvc.perform(
            get("/api/v1/{userUuid}/reviews", userUuid)
                .param("restaurant_uuid", restaurantUuid)
                .param("status", ReviewStatus.CREATED.name)
                .param("page", "1")
                .param("size", "20")
        )

        // then
        verify(exactly = 1) { findReviewsByUserUuid.execute(any()) }

        assertThat(result).isNotNull
        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").doesNotExist())
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.page").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total_pages").value(1))

        // and
        val reviewPageableCaptured = reviewPageableSlot.captured
        assertThat(reviewPageableCaptured).isNotNull
        assertThat(reviewPageableCaptured.userUuid).isEqualTo(UUID.fromString(userUuid))
        assertThat(reviewPageableCaptured.restaurantUuid).isEqualTo(UUID.fromString(restaurantUuid))
        assertThat(reviewPageableCaptured.status).isEqualTo(ReviewStatus.CREATED)
        assertThat(reviewPageableCaptured.page).isEqualTo(1)
        assertThat(reviewPageableCaptured.size).isEqualTo(20)
    }

    @Test
    fun whenExecuteFindReviewsWithOneResult_thenReturnOkAndListWithOneReview() {
        // given
        val userUuid = "dbb9c2bd-abde-48a3-891a-6229fc9b7c21"

        val review = Instancio.of(Review::class.java)
            .set(field("text"), "")
            .set(field("userUuid"), UUID.fromString(userUuid))
            .create()

        every { findReviewsByUserUuid.execute(any()) } returns PageImpl(arrayListOf(review))

        // when
        val result: ResultActions = mockMvc.perform(
            get("/api/v1/{userUuid}/reviews", userUuid)
        )

        // then
        verify(exactly = 1) { findReviewsByUserUuid.execute(any()) }

        assertThat(result).isNotNull
        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").doesNotExist())
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.page").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total_pages").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].uuid").value(review.uuid.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].status").value(review.status.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].restaurant.uuid").value(review.restaurant.uuid.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].restaurant.name").value(review.restaurant.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].text").isEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].order_uuid").value(review.orderUuid.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].user_uuid").value(review.userUuid.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].created_at").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].updated_at").isNotEmpty)
    }

    @Test
    fun whenExecuteFindReviewsWithResults_thenReturnOkAndListOfReviews() {
        // given
        val userUuid = "dbb9c2bd-abde-48a3-891a-6229fc9b7c21"

        val modelReview = Instancio.of(Review::class.java)
            .set(field("text"), "")
            .set(field("userUuid"), UUID.fromString(userUuid))
            .toModel()
        val reviews = Instancio.of(modelReview).stream().limit(3)
            .collect(Collectors.toList())

        every { findReviewsByUserUuid.execute(any()) } returns PageImpl(reviews)

        // when
        val result: ResultActions = mockMvc.perform(
            get("/api/v1/{userUuid}/reviews", userUuid)
        )

        // then
        verify(exactly = 1) { findReviewsByUserUuid.execute(any()) }

        assertThat(result).isNotNull
        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").doesNotExist())
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.page").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total_pages").value(1))
    }

    @Test
    fun whenExecuteFindReviewWithResult_thenReturnOkAndReviewBody() {
        // given
        val userUuid = "dbb9c2bd-abde-48a3-891a-6229fc9b7c21"
        val uuid = "f58ceb5b-6e70-489c-907b-0efe3b2feca5"

        val review = Instancio.of(Review::class.java)
            .set(field("uuid"), UUID.fromString(uuid))
            .set(field("text"), "")
            .set(field("userUuid"), UUID.fromString(userUuid))
            .create()

        every { findReviewFromUserUuidByUuid.execute(review.userUuid, review.uuid) } returns review

        // when
        val result: ResultActions = mockMvc.perform(
            get("/api/v1/{userUuid}/reviews/{uuid}", userUuid, uuid)
        )

        // then
        verify(exactly = 1) { findReviewFromUserUuidByUuid.execute(review.userUuid, review.uuid) }

        assertThat(result).isNotNull
        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").doesNotExist())
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.uuid").value(review.uuid.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.status").value(review.status.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.restaurant.uuid").value(review.restaurant.uuid.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.restaurant.name").value(review.restaurant.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.text").isEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.order_uuid").value(review.orderUuid.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.user_uuid").value(review.userUuid.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.created_at").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.updated_at").isNotEmpty)
    }

    @Test
    fun whenExecuteFindReviewAndHasException_thenReturnInternalServerError() {
        // given
        val userUuid = "dbb9c2bd-abde-48a3-891a-6229fc9b7c21"
        val uuid = "f58ceb5b-6e70-489c-907b-0efe3b2feca5"

        every { findReviewFromUserUuidByUuid.execute(UUID.fromString(userUuid), UUID.fromString(uuid)) } throws
                Exception("Unexpected Error")

        // when
        val result: ResultActions = mockMvc.perform(
            get("/api/v1/{userUuid}/reviews/{uuid}", userUuid, uuid)
        )

        // then
        verify(exactly = 1) { findReviewFromUserUuidByUuid.execute(UUID.fromString(userUuid), UUID.fromString(uuid)) }

        assertThat(result).isNotNull
        result.andExpect(MockMvcResultMatchers.status().isInternalServerError)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist())
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.error.code").value("9999"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("Unexpected Error"))
    }

    @Test
    fun whenExecuteFindReviewWithoutResult_thenReturnNotFoundAndErrorBody() {
        // given
        val userUuid = "dbb9c2bd-abde-48a3-891a-6229fc9b7c21"
        val uuid = "f58ceb5b-6e70-489c-907b-0efe3b2feca5"

        every { findReviewFromUserUuidByUuid.execute(UUID.fromString(userUuid), UUID.fromString(uuid)) } throws
                EntityNotFoundException("0001", "entityNotFoundException", "Review doesn't exists")

        // when
        val result: ResultActions = mockMvc.perform(
            get("/api/v1/{userUuid}/reviews/{uuid}", userUuid, uuid)
        )

        // then
        verify(exactly = 1) { findReviewFromUserUuidByUuid.execute(UUID.fromString(userUuid), UUID.fromString(uuid)) }

        assertThat(result).isNotNull
        result.andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist())
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.error.app").value("review-app"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.error.code").value("0001"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("Review doesn't exists"))
    }

    @Test
    fun whenExecuteAddCommentaryWithNoUserUuidAndUuidRegistered_thenReturnNotFoundAndErrorBody() {
        // given
        val userUuid = "dbb9c2bd-abde-48a3-891a-6229fc9b7c21"
        val uuid = "f58ceb5b-6e70-489c-907b-0efe3b2feca5"

        every { addCommentaryAtReview.execute(any()) } throws
                EntityNotFoundException("0001", "entityNotFoundException", "Review doesn't exists")

        val body = objectMapper.writeValueAsBytes(ReviewRequest("Good food!"))

        // when
        val result: ResultActions = mockMvc.perform(
            patch("/api/v1/{userUuid}/reviews/{uuid}", userUuid, uuid)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body)
        )

        // then
        verify(exactly = 1) { addCommentaryAtReview.execute(any()) }

        assertThat(result).isNotNull
        result.andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist())
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.error.app").value("review-app"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.error.code").value("0001"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("Review doesn't exists"))
    }

    @Test
    fun whenExecuteAddCommentary_thenReturnNoContent() {
        // given
        val userUuid = "dbb9c2bd-abde-48a3-891a-6229fc9b7c21"
        val uuid = "f58ceb5b-6e70-489c-907b-0efe3b2feca5"

        val reviewRequest = ReviewRequest("Good food!")

        val commentarySlot = slot<Commentary>()

        justRun { addCommentaryAtReview.execute(capture(commentarySlot)) }

        val body = objectMapper.writeValueAsBytes(reviewRequest)

        // when
        val result: ResultActions = mockMvc.perform(
            patch("/api/v1/{userUuid}/reviews/{uuid}", userUuid, uuid)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body)
        )

        // then
        verify(exactly = 1) { addCommentaryAtReview.execute(any()) }

        assertThat(result).isNotNull
        result.andExpect(MockMvcResultMatchers.status().isNoContent)

        // and
        val commentaryCaptured = commentarySlot.captured
        assertThat(commentaryCaptured.userUuid).isEqualTo(UUID.fromString(userUuid))
        assertThat(commentaryCaptured.uuid).isEqualTo(UUID.fromString(uuid))
        assertThat(commentaryCaptured.text).isEqualTo(reviewRequest.text)
    }
}
