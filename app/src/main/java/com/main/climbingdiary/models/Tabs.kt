package com.main.climbingdiary.models

import java.util.*

enum class Tabs {
    STATISTIK, ROUTEN, BOULDER, PROJEKTE;

    companion object{
        fun stringToTabs(type: String?): Tabs? {
            var result: Tabs? = null
            try {
                result = valueOf(type!!.uppercase(Locale.ROOT))
            } catch (ignored: Exception) {
            }
            return result
        }
    }

    open fun typeToString(): String? {
        var result: String? = null
        try {
            result = this.toString().lowercase(Locale.ROOT)
        } catch (ignored: java.lang.Exception) {
        }
        return result
    }
}