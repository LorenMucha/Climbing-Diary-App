package com.main.climbingdiary.models

import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity

enum class TimeRange {
    YEAR, RANGE;

    companion object {
        private val ressourceArray: Array<String> = MainActivity.getMainActivity()
            .resources.getStringArray(R.array.entries_time_slider)
        private val translateMap = mapOf(
            ressourceArray[0] to YEAR,
            ressourceArray[1] to RANGE
        )
        fun translate(value: String): TimeRange? {
            return translateMap[value]
        }
        //Fixme
        fun translate(value: TimeRange): String{
            for((k,v) in translateMap){
                if(v == value){
                    return k
                }
            }
            throw RuntimeException("")
        }
    }
}