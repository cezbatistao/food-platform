package com.food.review.gateway

import com.food.review.domain.Review

interface ReviewSendGateway {

    fun sendCreated(review: Review)

    fun sendVerified(review: Review)

}