package com.food.review.entrypoint.rest

import com.food.review.domain.Commentary
import com.food.review.domain.ReviewPageable
import com.food.review.domain.ReviewStatus
import com.food.review.entrypoint.rest.json.response.DataResponse
import com.food.review.entrypoint.rest.json.response.ListDataResponse
import com.food.review.entrypoint.rest.json.request.ReviewRequest
import com.food.review.entrypoint.rest.json.response.ReviewResponse
import com.food.review.entrypoint.rest.mapper.ReviewResponseMapper
import com.food.review.entrypoint.rest.mapper.ReviewResponseMapper.Companion
import com.food.review.usecase.AddCommentaryAtReview
import com.food.review.usecase.FindReviewFromUserUuidByUuid
import com.food.review.usecase.FindReviewsByUserUuid
import java.util.UUID
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/api")
class ReviewController(
    val findReviewsByUserUuid: FindReviewsByUserUuid,
    val findReviewFromUserUuidByUuid: FindReviewFromUserUuidByUuid,
    val addCommentaryAtReview: AddCommentaryAtReview
) {

    @GetMapping(
        path = ["/v1/{userUuid}/reviews"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getReviewsFromUserUuid(
            @PathVariable("userUuid") userUuid: String,
            @RequestParam(name = "restaurant_uuid", required = false) restaurantUuid: String?,
            @RequestParam(name = "status", required = false) status: ReviewStatus?,
            @RequestParam(name = "page", required = true, defaultValue = "0") page: Int,
            @RequestParam(name = "size", required = true, defaultValue = "10") size: Int
    ): ResponseEntity<ListDataResponse<ReviewResponse>> {
        val reviewPageable = ReviewPageable(
            UUID.fromString(userUuid), restaurantUuid?.let { UUID.fromString(restaurantUuid) }, status, page, size)
        val reviewPage = this.findReviewsByUserUuid.execute(reviewPageable)
        return ResponseEntity.ok(
            ListDataResponse(
            Companion.INSTANCE.map(reviewPage.toList()),
            reviewPage.number,
            reviewPage.totalPages,
            reviewPage.size,
            reviewPage.totalElements)
        )
    }

    @GetMapping(
        path = ["/v1/{userUuid}/reviews/{uuid}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getReviewByUuid(
        @PathVariable("userUuid") userUuid: String,
        @PathVariable("uuid") uuid: String,
    ): ResponseEntity<DataResponse<ReviewResponse>> {
        val review = this.findReviewFromUserUuidByUuid.execute(UUID.fromString(userUuid), UUID.fromString(uuid))
        return ResponseEntity.ok(DataResponse(ReviewResponseMapper.INSTANCE.map(review)))
    }

    @PatchMapping(
        path = ["/v1/{userUuid}/reviews/{uuid}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun addCommentary(
        @PathVariable("userUuid") userUuid: String,
        @PathVariable("uuid") uuid: String,
        @RequestBody reviewRequest: ReviewRequest
    ): ResponseEntity<Void> {
        val commentary = Commentary(UUID.fromString(userUuid), UUID.fromString(uuid), reviewRequest.text)
        this.addCommentaryAtReview.execute(commentary)
        return ResponseEntity.noContent().build()
    }
}
