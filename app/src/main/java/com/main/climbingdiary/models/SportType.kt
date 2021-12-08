package com.main.climbingdiary.models

import java.util.*

enum class SportType {
    KLETTERN, BOULDERN;
    
    companion object{
        fun stringToSportType(type: String?): SportType {
            return valueOf(type!!.uppercase(Locale.ROOT))
        }
    }

    fun typeToString(): String {
        return this.toString().lowercase(Locale.ROOT)
    }

    //Fixme: gehört nicht hierher
    fun getRouteName(): String {
        return if (this == KLETTERN) "Routen" else "Boulder"
    }
}