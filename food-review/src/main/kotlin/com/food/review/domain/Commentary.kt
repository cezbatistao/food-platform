package com.food.review.domain

import java.util.UUID

data class Commentary(val userUuid: UUID, val uuid: UUID, val text: String)
