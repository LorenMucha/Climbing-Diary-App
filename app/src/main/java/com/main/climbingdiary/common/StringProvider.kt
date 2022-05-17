package com.main.climbingdiary.common

import com.main.climbingdiary.activities.MainActivity

object StringProvider {
    private val context by lazy {
        MainActivity.getMainAppContext()
    }

    fun getString(id:Int):String{
        return context.resources.getString(id)
    }
}