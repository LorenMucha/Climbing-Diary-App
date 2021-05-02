package com.main.climbingdiary.common.preferences

import com.main.climbingdiary.model.SportType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class SportTypeTest {

    @ParameterizedTest
    @MethodSource("sportTypeArguments")
    fun `fiven input must value of SportType`(input: String, expected: SportType){
        val transformVal = SportType.valueOf(input)
        assertEquals(transformVal, expected)
    }

    private companion object{
        @JvmStatic
        fun sportTypeArguments() = Stream.of(
            Arguments.of("KLETTERN", SportType.KLETTERN),
            Arguments.of("BOULDERN", SportType.BOULDERN),
            Arguments.of("KLETTERN", SportType.KLETTERN)
        )
    }
}