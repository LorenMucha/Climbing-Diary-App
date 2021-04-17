package com.main.climbingdiary.models;

import com.main.climbingdiary.common.GradeConverter;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RouteSortTest {

    @ParameterizedTest
    @MethodSource("provideStringsForTest")
    public void testRouteSortConverter(RouteSort input, RouteSort expected){
        assertEquals(input, expected);
    }

    private static Stream<Arguments> provideStringsForTest() {
        return Stream.of(
                Arguments.of(RouteSort.DATE,  RouteSort.stringToSportType("date")),
                Arguments.of(RouteSort.LEVEL,  RouteSort.stringToSportType("level")),
                Arguments.of(RouteSort.AREA,  RouteSort.stringToSportType("area"))
        );
    }
}
