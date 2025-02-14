package com.main.climbingdiary.common

import android.util.Log
import com.main.climbingdiary.models.Levels.getLevelsFrench
import com.main.climbingdiary.models.Levels.getLevelsUiaa

object GradeConverter {
    @JvmStatic
    fun convertUiaaToFrench(gradeUiaa: String): String {
        val grade = convertNumbersToRoman(gradeUiaa)
        return try {
            val pos = listOf(*getLevelsUiaa()).indexOf(convertNumbersToRoman(grade))
            getLevelsFrench()[pos]
        } catch (ex: Exception) {
            return "Parsing Fehler bitte überarbeiten"
        }
    }

    @JvmStatic
    fun convertFrenchToUiaa(french: String): String {
        return try {
            val pos = listOf(*getLevelsFrench()).indexOf(french)
            getLevelsUiaa()[pos]
        } catch (ex: Exception) {
            return "Parsing Fehler bitte überarbeiten"
        }
    }

    @JvmStatic
    fun convertAnyToFrench(grade: String): String {
        return when {
                Regex("^[I-VX]+[+-]?$|^\\d{1,2}[+-]?$|^\\d{1,2}[+-]?/\\d{1,2}[+-]?$", RegexOption.IGNORE_CASE).matches(grade) -> convertUiaaToFrench(grade)
                Regex("^[1-9][abc]?\\+?$", RegexOption.IGNORE_CASE).matches(grade) -> grade
                else -> "Parsing Fehler bitte überarbeiten"
            }
    }

    @JvmStatic
    fun convertNumbersToRoman(grade: String): String {
        val romanNumerals = mapOf(
            1 to "I", 4 to "IV", 5 to "V", 9 to "IX",
            10 to "X", 40 to "XL", 50 to "L", 90 to "XC",
            100 to "C", 400 to "CD", 500 to "D", 900 to "CM", 1000 to "M"
        )

        // Hilfsmethode zur Umwandlung von arabischen Zahlen zu römischen Zahlen
        fun arabicToRoman(number: Int): String {
            var num = number
            var result = ""
            val sortedValues = romanNumerals.keys.sortedDescending()

            for (value in sortedValues) {
                while (num >= value) {
                    result += romanNumerals[value]
                    num -= value
                }
            }
            return result
        }

        // Funktion, um zu prüfen, ob der String eine römische Zahl ist
        fun isRoman(grade: String): Boolean {
            return Regex("^[IVXLCDM]+$", RegexOption.IGNORE_CASE).matches(grade)
        }

        // Umwandlung der Eingabe
        return grade.split("/").joinToString("/") { part ->
            val sign = part.takeLastWhile { it == '+' || it == '-' } // Extrahieren von + oder -
            val numberPart = part.removeSuffix("+").removeSuffix("-") // Entfernen von + und - für die Umwandlung

            if (isRoman(numberPart)) {
                part // Wenn es sich um eine römische Zahl handelt, bleibt sie unverändert
            } else {
                arabicToRoman(numberPart.toIntOrNull() ?: 0) + sign // Andernfalls wird sie in eine römische Zahl umgewandelt, + oder - wird wieder angehängt
            }
        }
    }
}