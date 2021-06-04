package com.main.climbingdiary.common

import android.util.Log
import com.main.climbingdiary.model.Levels

object GradeConverter {
    fun convertUiaaToFrench(gradeUiaa: String): String {
        return try {
            val pos: Int = listOf(Levels.getLevelsUiaa()).indexOf(gradeUiaa)
            Levels.getLevelsFrench()[pos]
        } catch (ex: Exception) {
            Log.d("GradeConverter", ex.localizedMessage)
        }.toString()
    }

    fun convertFrenchToUiaa(french: String): String {
        return try {
            val pos: Int = listOf(Levels.getLevelsFrench()).indexOf(french)
            Levels.getLevelsUiaa()[pos]
        } catch (ex: Exception) {
            Log.d("GradeConverter", ex.localizedMessage)
        }.toString()
    }
}