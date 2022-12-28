package com.food.review.gateway.kafka.resource.producer

class ReviewEvent(val uuid: String,
                  val userUuid: String,
                  val orderUuid: String,
                  val restaurantUuid: String,
                  val text: String)
