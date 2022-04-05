package com.main.climbingdiary.common

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import java.util.*


class LanguageManager(private val context: Context) {

    private val language by lazy {
        AppPreferenceManager.getLanguage()
    }

    fun setLanguage() {
        val locale = Locale(language!!)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    fun switchLanguage() {
        when (language) {
            "de" -> AppPreferenceManager.setLanguage("en")
            "en" -> AppPreferenceManager.setLanguage("de")
        }
        Log.d("Language", AppPreferenceManager.getLanguage().toString())
        setLanguage()
    }
}