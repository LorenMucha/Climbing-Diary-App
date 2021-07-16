package com.main.climbingdiary.models

import java.util.*

object Styles {

    fun getRP(lowercase: Boolean = false): String {
        return if (lowercase) "rp" else "RP"
    }

    fun getOS(lowercase: Boolean = false): String {
        return if (lowercase) "os" else "OS"
    }

    fun getFLASH(lowercase: Boolean = false): String {
        return if (lowercase) "flash" else "FLASH"
    }

    fun getStyle(UpperCase: Boolean): Array<String> {
        return if (UpperCase) {
            arrayOf(getOS(), getRP(), getFLASH())
        } else {
            arrayOf(getOS(true), getRP(true), getFLASH(true))
        }
    }

    fun getStyleRatingFactor(style: String): Int {
        return listOf(*getStyle(false))
            .indexOf(style.toLowerCase(Locale.ROOT)) * 5
    }
}