package com.main.climbingdiary.model

import java.util.*

enum class SportType {
    KLETTERN, BOULDERN;

    fun typeOf(string: String): SportType{
        return SportType.valueOf(string.toUpperCase(Locale.ROOT))
    }
}