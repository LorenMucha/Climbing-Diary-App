package com.main.climbingdiary.activities

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.main.climbingdiary.R

class MainActivity : AppCompatActivity() {

    companion object{
        var APPLICATION: AppCompatActivity? = null
        var COMPONENT_NAME: ComponentName? = null
        var CONTEXT: Context? = null
        fun getMainActivity(): AppCompatActivity? = APPLICATION
        fun getComponentName(): ComponentName? = COMPONENT_NAME
        fun getContext(): Context? = CONTEXT
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        APPLICATION = this
        COMPONENT_NAME= componentName
        CONTEXT = applicationContext
    }
}