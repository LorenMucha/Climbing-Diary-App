package com.main.climbingdiary.models

import java.util.*

enum class SportType {
    KLETTERN, BOULDERN;
    
    companion object{
        fun stringToSportType(type: String?): SportType {
            return valueOf(type!!.toUpperCase(Locale.ROOT))
        }
    }

    fun typeToString(): String {
        return this.toString().toLowerCase(Locale.ROOT)
    }

    fun getRouteName(): String {
        return if (this == KLETTERN) "Routen" else "Boulder"
    }
}