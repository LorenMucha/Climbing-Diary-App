package com.main.climbingdiary.model

import java.util.*

object Styles {

    fun getStyle(UpperCase: Boolean): Array<String> {
        return if (UpperCase) {
            arrayOf("OS", "RP", "FLASH")
        } else {
            arrayOf("os", "rp", "flash")
        }
    }

    fun getStyleRatingFactor(style: String): Int {
        return listOf(*getStyle(false))
            .indexOf(style.toLowerCase(Locale.ROOT)) * 5
    }
}