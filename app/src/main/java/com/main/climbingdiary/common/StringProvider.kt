package com.main.climbingdiary.common

import com.main.climbingdiary.activities.MainActivity

object StringProvider {
    private val showCase by lazy {
        MainActivity.getMainAppContext().resources
    }

    fun getString(id:Int):String{
        return showCase.getString(id)
    }
}