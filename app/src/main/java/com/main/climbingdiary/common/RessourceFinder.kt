package com.main.climbingdiary.common

import com.main.climbingdiary.activities.MainActivity

object RessourceFinder {

    private val activity by lazy { MainActivity.getMainActivity() }

    fun getStringRessourceById(id:Int): String{
        return activity.getString(id)
    }
}