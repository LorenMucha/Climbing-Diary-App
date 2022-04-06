package com.main.climbingdiary.common

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

class AppHelper(private val context:Context?) {
    fun restartApp(){
        val packageManager: PackageManager = context!!.packageManager
        val intent =
            packageManager.getLaunchIntentForPackage(context.packageName)!!
        val componentName = intent.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        context.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }
}