package com.main.climbingdiary.models

import com.main.climbingdiary.R
import com.main.climbingdiary.common.StringManager.getStringForId
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.error.TabsNotSupportedException

enum class Tabs(val s: String) {
    STATISTIK("S"), ROUTEN("R"), BOULDER("B"), PROJEKTE("P");

    companion object{
        fun stringToTabs(tabName: String?): Tabs {
            val firstLetter = tabName!!.uppercase()[0].toString()
            values().forEach {
                if(it.s == firstLetter){
                    return it
                }
            }
            throw TabsNotSupportedException("$tabName not supported")
        }
    }

    fun typeToString(): String {
       return when(this){
            STATISTIK -> getStringForId(R.string.tab_statistic)
            ROUTEN -> {
                //needed for initialize start of the app
                if(AppPreferenceManager.getSportType() == SportType.BOULDERN){
                    getStringForId(R.string.tabs_boulder)
                }else {
                    getStringForId(R.string.tabs_routen)
                }
            }
            BOULDER -> getStringForId(R.string.tabs_boulder)
            PROJEKTE -> getStringForId(R.string.tabs_projects)
        }
    }
}