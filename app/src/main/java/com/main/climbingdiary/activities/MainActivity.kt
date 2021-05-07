package com.main.climbingdiary.activities

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.main.climbingdiary.R
import com.main.climbingdiary.common.AppPreferenceManager

class MainActivity : AppCompatActivity() {

    companion object{
        @Volatile var APPLICATION: AppCompatActivity? = null
        @Volatile var COMPONENT_NAME: ComponentName? = null
        @SuppressLint("StaticFieldLeak")
        @Volatile var CONTEXT: Context? = null
        @Synchronized fun getApplication(): AppCompatActivity? = APPLICATION
        @Synchronized fun getAppContext(): Context? = CONTEXT
        @Synchronized fun getComponentName(): ComponentName? = COMPONENT_NAME
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        APPLICATION = this
        COMPONENT_NAME= componentName
        CONTEXT = applicationContext

        AppPreferenceManager.removeAllFilterPrefs()


    }
}