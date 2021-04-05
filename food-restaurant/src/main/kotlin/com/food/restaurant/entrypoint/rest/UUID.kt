package com.food.restaurant.entrypoint.rest

import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.constraints.Pattern
import kotlin.reflect.KClass

@Target(AnnotationTarget.VALUE_PARAMETER)
@Constraint(validatedBy=[])
@Retention(AnnotationRetention.RUNTIME)
@Pattern(regexp="^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")
annotation class Uuid (
    val message: String = "{invalid.uuid}",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)
