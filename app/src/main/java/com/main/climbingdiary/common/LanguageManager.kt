package com.main.climbingdiary.common

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.main.climbingdiary.R
import com.main.climbingdiary.common.StringProvider.getString
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.models.Alert
import java.util.*


class LanguageManager(private val context: Context) {

    companion object {
        const val DE = "de"
        const val EN = "en"
    }

    private val language by lazy {
        AppPreferenceManager.getLanguage()
    }

    fun setLanguage(language: String = AppPreferenceManager.getLanguage()!!) {
        changeLocale(language)
        if (AppPreferenceManager.getLanguageFirstTime()) {
            initLanguage()
        }
    }

    private fun changeLocale(language: String){
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    fun switchLanguage(restart: Boolean = false) {
        when (language) {
            DE -> AppPreferenceManager.setLanguage(EN)
            EN -> AppPreferenceManager.setLanguage(DE)
        }
        setLanguage()
        restart.let {
            if (it) {
                val alert = AlertFactory.getAlert(
                    context,
                    Alert(
                        title = StringManager.getStringForId(R.string.alert_language_change),
                        message = StringManager.getStringForId(R.string.app_will_restart)
                    )
                )
                    .setConfirmText(getString(R.string.app_ok))
                    .setConfirmClickListener {
                        AppHelper(context).restartApp()
                    }
                alert.show()
            }
        }
    }

    private fun initLanguage() {
        val dialog = Dialog(context)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_language_btn_group)
        val germanBtn: ImageButton = dialog.findViewById(R.id.btn_welcome_de)
        val englishBtn: ImageButton = dialog.findViewById(R.id.btn_welcome_en)
        germanBtn.setOnClickListener {
            onLanguageBtnClick(EN)
        }
        englishBtn.setOnClickListener {
           onLanguageBtnClick()
        }
        dialog
            .setTitle("Test")
            .let{dialog.show()}
    }

    private fun onLanguageBtnClick(lang:String= DE){
        AppPreferenceManager.setLanguage(lang)
        AppPreferenceManager.setLanguageFirstTime(isUsed = false)
        LanguageManager(context).switchLanguage(true)
    }
}