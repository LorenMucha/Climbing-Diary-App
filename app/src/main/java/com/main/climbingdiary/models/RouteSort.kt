package com.main.climbingdiary.models

import android.util.Log
import java.util.*

enum class RouteSort {
    LEVEL, AREA, DATE;

    companion object {
        @JvmStatic
        fun stringToSportType(type: String?): RouteSort? {
            var value: RouteSort? = null
            try {
                value = valueOf(type!!.toUpperCase(Locale.ROOT))
            } catch (ex: Exception) {
                Log.d("Exception in MenuValues stringToSportType() ", ex.localizedMessage)
            }
            return value
        }
    }

    fun typeToString(): String {
        return this.toString().toLowerCase(Locale.ROOT)
    }
}