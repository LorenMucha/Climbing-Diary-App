package com.main.climbingdiary.models

import android.graphics.Color
import com.main.climbingdiary.R
import java.util.*

object Colors {
    val mainColor: Int
        get() = R.color.colorPrimary
    val activeColor: Int
        get() = R.color.colorAccent
    val buttonColor: Int
        get() = R.color.buttonColor
    val warningColor: Int
        get() = Color.parseColor("#FF8800")
    val dangerColor: Int
        get() = Color.parseColor("#CC0000")

    //must be a string1
    fun getGradeColor(_grade: String): Int {
        return if (_grade.contains("8")) {
            Color.parseColor("#212121")
        } else if (_grade.contains("7")) {
            Color.parseColor("#bf360c")
        } else {
            Color.parseColor("#FF8800")
        }
    }

    fun getStyleColor(_style: String): Int {
        return when (_style.toLowerCase(Locale.ROOT)) {
            "os" -> Color.parseColor("#33b5e5")
            "rp" -> Color.parseColor("#0d47a1")
            else -> Color.parseColor("#00C851")
        }
    }
}