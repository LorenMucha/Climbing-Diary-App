package com.main.climbingdiary.common

import android.util.Log
import com.main.climbingdiary.models.Levels.getLevelsFrench
import com.main.climbingdiary.models.Levels.getLevelsUiaa

object GradeConverter {
    @JvmStatic
    fun convertUiaaToFrench(gradeUiaa: String): String? {
        return try {
            val pos = listOf(*getLevelsUiaa()).indexOf(gradeUiaa)
            getLevelsFrench()[pos]
        } catch (ex: Exception) {
            Log.d("GradeConverter", ex.localizedMessage)
            null
        }
    }
    @JvmStatic
    fun convertFrenchToUiaa(french: String): String? {
        return try {
            val pos = listOf(*getLevelsFrench()).indexOf(french)
            getLevelsUiaa()[pos]
        } catch (ex: Exception) {
            Log.d("GradeConverter", ex.localizedMessage)
            null
        }
    }
}