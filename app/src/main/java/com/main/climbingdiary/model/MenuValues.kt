package com.main.climbingdiary.model

enum class MenuValues {
    SEARCH, FILTER, SORT, SORT_DATE, SETTINGS;

    override fun toString():String{
        return this.toString().replace("SORT_","")
    }
}