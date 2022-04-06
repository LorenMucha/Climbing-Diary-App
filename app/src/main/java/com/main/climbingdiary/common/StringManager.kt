package com.main.climbingdiary.common

import android.content.Context
import android.content.res.Configuration
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import java.util.*

object StringManager {
    private val context: Context by lazy { MainActivity.getMainAppContext() }

    fun getStringForId(int: Int): String {
        return context.getLocaleStringResource(Locale(AppPreferenceManager.getLanguage()!!), int)
    }

    private fun Context.getLocaleStringResource(
        requestedLocale: Locale?,
        resourceId: Int,
    ): String {
        val result: String
        val config =
            Configuration(resources.configuration)
        config.setLocale(requestedLocale)
        result = createConfigurationContext(config).getText(resourceId).toString()

        return result
    }
}