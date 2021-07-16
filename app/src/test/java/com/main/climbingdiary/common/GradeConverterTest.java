package com.main.climbingdiary.common;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GradeConverterTest {


    private static Stream<Arguments> provideStringsForTest() {
        return Stream.of(
                Arguments.of("IX+/X-", GradeConverter.convertFrenchToUiaa("8a")),
                Arguments.of(null, GradeConverter.convertFrenchToUiaa("22")),
                Arguments.of("6a", GradeConverter.convertUiaaToFrench("VI+")),
                Arguments.of(null, GradeConverter.convertUiaaToFrench("test")),
                Arguments.of("7c", GradeConverter.convertUiaaToFrench("IX")),
                Arguments.of("5c", GradeConverter.convertUiaaToFrench("VI")),
                Arguments.of("VIII+", GradeConverter.convertFrenchToUiaa("7a+"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideStringsForTest")
    public void testGradeConverter(String input, String expected) {
        assertEquals(input, expected);
    }

}
