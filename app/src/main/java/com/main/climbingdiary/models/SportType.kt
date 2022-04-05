package com.main.climbingdiary.models

import com.main.climbingdiary.R
import com.main.climbingdiary.common.StringManager
import com.main.climbingdiary.error.SportTypeNotSupportedExeption
import java.util.*

enum class SportType {
    KLETTERN, BOULDERN;
    
    companion object{
        fun stringToSportType(type: String?): SportType {
            return when(type!!.uppercase()[0].toString()){
                "K","C"-> KLETTERN
                "B" -> BOULDERN
                else -> {
                    throw SportTypeNotSupportedExeption("$type not supported")
                }
            }
        }
    }

    fun typeToString(): String {
        return this.toString().lowercase(Locale.ROOT)
    }

    fun getRouteName(): String {
        return if (this == KLETTERN) StringManager.getStringForId(R.string.tabs_routen)
        else StringManager.getStringForId(R.string.tabs_boulder)
    }
}