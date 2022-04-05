package com.main.climbingdiary.fragments

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.main.climbingdiary.R
import com.main.climbingdiary.common.AlertFactory.getAlert
import com.main.climbingdiary.common.AppPermissions
import com.main.climbingdiary.common.LanguageManager
import com.main.climbingdiary.common.RessourceFinder
import com.main.climbingdiary.common.StringManager
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.common.preferences.AppPreferenceManager.getOutputPath
import com.main.climbingdiary.common.preferences.PreferenceKeys
import com.main.climbingdiary.controller.slider.TimeSliderFactory
import com.main.climbingdiary.models.Alert
import com.main.climbingdiary.models.TimeRange
import com.main.climbingdiary.provider.AppFileProvider
import java.io.File
import java.io.IOException

class SettingsFragment : PreferenceFragmentCompat() {

    private val dbOutputPath: Preference? by lazy {
        findPreference(RessourceFinder.getStringRessourceById(R.string.db_output_path))
    }
    private val fileChooserRequestCopy =
        PreferenceKeys.FILE_CHOOOSER_REQUEST_RESTORE_COPY
    private val fileChooserSafetyCopy = PreferenceKeys.FILE_CHOOOSER_REQUEST_SAFTY_COPY
    private val timeSliderView: ListPreference? by lazy {
        findPreference(RessourceFinder.getStringRessourceById(R.string.pref_time_slider))
    }
    private val languagePref:Preference? by lazy {
        findPreference(RessourceFinder.getStringRessourceById(R.string.language))
    }
    private val languageSet by lazy { AppPreferenceManager.getLanguage() }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
        val exportDb: Preference? =
            findPreference(RessourceFinder.getStringRessourceById(R.string.safty_copy))

        val saftyCopy: Preference? =
            findPreference(RessourceFinder.getStringRessourceById(R.string.safty_restore))

        //set the correct flag
        languagePref.let {
            when(languageSet){
                "de"-> it!!.setIcon(R.drawable.en)
                "en"-> it!!.setIcon(R.drawable.de)
            }
        }

        dbOutputPath?.setOnPreferenceClickListener {
            openFolderChooser()
            true
        }
        saftyCopy?.setOnPreferenceClickListener {
            openFileChooser()
            true
        }
        exportDb?.setOnPreferenceClickListener {
            exportDb()
            true
        }

        timeSliderView?.setOnPreferenceChangeListener { _, newValue ->
            val range = TimeRange.translate(newValue as String)
            AppPreferenceManager.setTimeSliderView(range)
            timeSliderView!!.summary = newValue
            TimeSliderFactory.reloadSlider()
            true
        }

        languagePref?.setOnPreferenceClickListener {
            changeLocale()
            true
        }

        initPrefs()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == fileChooserSafetyCopy && resultCode == RESULT_OK) {
            val selectedfile = data?.data!!
            val paths: List<String> = selectedfile.path!!.split(":")
            val completePath = Environment.getExternalStorageDirectory().toString() +
                    if (paths.size > 1) File.separator + paths[1] else ""
            AppPreferenceManager.setOutputPath(completePath)
            dbOutputPath!!.summary = completePath

        }
        if (requestCode == fileChooserRequestCopy && resultCode == RESULT_OK) {
            val path = data?.data
            restoreDB(path)
        }
    }

    private fun initPrefs() {
        try {
            dbOutputPath!!.summary = getOutputPath()
            //set the default value
            timeSliderView!!.let {
                if (it.value == null) {
                    it.setValueIndex(0)
                }
            }
            timeSliderView!!.summary = TimeRange.translate(AppPreferenceManager.getTimeSliderView())
        } catch (ignored: NullPointerException) {
        }
    }

    private fun openFolderChooser() {
        val i = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        i.addCategory(Intent.CATEGORY_DEFAULT)
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(
            Intent.createChooser(i, "Output-Pfad"),
            fileChooserSafetyCopy
        )
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, fileChooserRequestCopy)
    }

    private fun exportDb() {
        try {
            AppFileProvider().exportDBtoPreferencePath()
            this.context?.let {
                getAlert(
                    it,
                    Alert(
                        dialogType = SweetAlertDialog.SUCCESS_TYPE,
                        title = "Die Datenbank wurde exportiert !"
                    )
                ).show()
            }
        } catch (e: IOException) {
            this.context?.let { it ->
                val errorAlert = getAlert(
                    it,
                    Alert(
                        dialogType = SweetAlertDialog.ERROR_TYPE,
                        title = "Der Export ist Schiefgelaufen 😓"
                    )
                )
                errorAlert.setConfirmClickListener {
                    AppPermissions.checkFileManagementPermission(this.context)
                    it.cancel()
                }.show()
            }
        }
    }

    private fun restoreDB(path: Uri?) {
        try {
            if (path?.let { AppFileProvider().restoreDBfromPreferencePath(it) } == true) {
                val alert = getAlert(requireContext(),
                    Alert(title=getString(R.string.alert_db_restored_correctly),
                        message = getString(R.string.app_will_restart) ))
                    .setConfirmClickListener {
                        val context: Context? = this.context
                        val packageManager: PackageManager = context!!.packageManager
                        val intent =
                            packageManager.getLaunchIntentForPackage(context.packageName)!!
                        val componentName = intent.component
                        val mainIntent = Intent.makeRestartActivityTask(componentName)
                        context.startActivity(mainIntent)
                        Runtime.getRuntime().exit(0)
                    }
                    alert.show()
            }
        } catch (e: IOException) {
            Log.d("restoreDb", e.localizedMessage as String)
            this.context?.let {
                getAlert(
                    it,
                    Alert(
                        dialogType = SweetAlertDialog.ERROR_TYPE,
                        title = getString(R.string.alert_restore_db_failure)
                    )
                ).show()
            }
        }
    }

    private fun changeLocale(){
        LanguageManager(requireContext())
            .switchLanguage()
            .let {
                val alert = getAlert(requireContext(),
                    Alert(title=StringManager.getStringForId(R.string.alert_language_change),
                        message = StringManager.getStringForId(R.string.app_will_restart) ))
                    .setConfirmClickListener {
                        val context: Context? = context
                        val packageManager: PackageManager = context!!.packageManager
                        val intent =
                            packageManager.getLaunchIntentForPackage(context.packageName)!!
                        val componentName = intent.component
                        val mainIntent = Intent.makeRestartActivityTask(componentName)
                        context.startActivity(mainIntent)
                        Runtime.getRuntime().exit(0)
                    }
                alert.show()
            }
    }
}