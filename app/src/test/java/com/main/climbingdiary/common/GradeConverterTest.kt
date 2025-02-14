package com.main.climbingdiary.common

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GradeConverterTest {

    @ParameterizedTest
    @CsvSource("VIII-,6c+", "IX+/X-,8a", "IX-,7b+")
    fun convertUiaaToFrench(input: String, expected: String) {
        val actualValue = GradeConverter.convertUiaaToFrench(input)
        assertEquals(expected, actualValue)
    }

    @ParameterizedTest
    @CsvSource("VIII-,6c+", "IX+/X-,8a", "IX-,7b+")
    fun convertFrenchToUiaa(input: String, expected: String) {
        val actualValue = GradeConverter.convertFrenchToUiaa(expected)
        assertEquals(input, actualValue)
    }

    @ParameterizedTest
    @CsvSource("8+/9-, 7b", "9+, 7c+", "7+/8-, 6c")
    fun convertStringToCorrectScale(input: String, expected: String) {
        val actualValue = GradeConverter.convertAnyToFrench(input)
        assertEquals(actualValue, expected)
    }
}