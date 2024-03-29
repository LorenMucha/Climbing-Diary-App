package com.main.climbingdiary.models

object Levels {
    fun getLevelsFrench(): Array<String> {
        return arrayOf(
            "1",
            "2",
            "3",
            "4",
            "5a",
            "5a+",
            "5b",
            "5b+",
            "5c",
            "6a",
            "6a+",
            "6b",
            "6b+",
            "6c",
            "6c+",
            "7a",
            "7a+",
            "7b",
            "7b+",
            "7c",
            "7c+",
            "8a",
            "8a+",
            "8b",
            "8b+",
            "8c",
            "8c+",
            "9a"
        )
    }

    fun getLevelsUiaa(): Array<String> {
        return arrayOf(
            "I",
            "II",
            "III",
            "IV",
            "V-",
            "V",
            "V+",
            "VI-",
            "VI",
            "VI+",
            "VII-",
            "VII",
            "VII+",
            "VII+/VIII−",
            "VIII-",
            "VIII",
            "VIII+",
            "VIII+/IX−",
            "IX-",
            "IX",
            "IX+",
            "IX+/X-",
            "X-",
            "X",
            "X+",
            "XI-",
            "XI−/XI",
            "XI"
        )
    }

    /*
    Method to build a weight for each grade, used in statistical overview
    @see com.main.climbingdiary.chart.RouteLineChartController.class
    */
    fun getLevelRating(level: String): Int {
        return listOf(*getLevelsFrench()).indexOf(level) * 5
    }
}