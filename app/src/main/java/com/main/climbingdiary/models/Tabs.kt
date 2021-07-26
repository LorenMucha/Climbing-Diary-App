package com.main.climbingdiary.models

import java.util.*

enum class Tabs {
    STATISTIK, ROUTEN, BOULDER, PROJEKTE, MAP;

    companion object{
        @JvmStatic
        fun stringToTabs(type: String): Tabs? {
            var result: Tabs? = null
            try {
                result = valueOf(type.toUpperCase(Locale.ROOT))
            } catch (ignored: Exception) {
            }
            return result
        }
    }

    open fun typeToString(): String? {
        var result: String? = null
        try {
            result = this.toString().toLowerCase(Locale.ROOT)
        } catch (ignored: java.lang.Exception) {
        }
        return result
    }
}