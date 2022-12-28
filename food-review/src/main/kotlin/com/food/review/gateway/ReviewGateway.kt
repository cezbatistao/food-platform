package com.food.review.gateway

import com.food.review.domain.Review

interface ReviewGateway {

    fun save(review: Review): Review

}