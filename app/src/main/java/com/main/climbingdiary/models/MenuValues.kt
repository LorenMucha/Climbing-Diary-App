package com.main.climbingdiary.models

import java.util.*


enum class MenuValues {
    SEARCH, FILTER, SORT, SORT_DATE, SETTINGS;

    companion object {
        fun stringToSportType(type: String?): MenuValues {
            return valueOf(type!!.toUpperCase(Locale.ROOT))
        }
    }

    fun typeToString(): String {
        return this.toString().toLowerCase(Locale.ROOT)
    }

    override fun toString(): String {
        var value = ""
        value = when (this) {
            SEARCH -> "search"
            FILTER -> "filter"
            SORT -> "sort"
            SORT_DATE -> "date"
            SETTINGS -> "settings"
        }
        return value
    }
}