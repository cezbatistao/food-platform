package com.food.review.config.log

import com.google.common.base.CaseFormat
import java.util.Locale

enum class LogKey {

    TOPIC,
    DATA,
    REVIEW;

    override fun toString(): String {
        return CaseFormat.UPPER_UNDERSCORE.to(
            CaseFormat.LOWER_CAMEL, name.lowercase(Locale.getDefault())
        )
    }
}
