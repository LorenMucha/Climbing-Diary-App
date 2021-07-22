package com.main.climbingdiary.models

import java.util.*

enum class MenuValues {
    SEARCH, FILTER, SORT, SORT_DATE, SETTINGS;

    companion object {
        fun stringToSportType(type: String): MenuValues? {
            return valueOf(type.toUpperCase(Locale.ROOT))
        }
    }

    open fun typeToString(): String {
        return this.toString().toLowerCase(Locale.ROOT)
    }

    override fun toString(): String {
        return this.toString().replace("SORT_", "")
    }
}