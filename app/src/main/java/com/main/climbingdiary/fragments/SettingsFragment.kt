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
import com.main.climbingdiary.common.AlertManager.setAlertWithoutContent
import com.main.climbingdiary.common.AppFileProvider
import com.main.climbingdiary.common.RessourceFinder
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.common.preferences.AppPreferenceManager.getOutputPath
import com.main.climbingdiary.common.preferences.PreferenceKeys
import com.main.climbingdiary.models.Alert
import com.main.climbingdiary.models.TimeRange
import java.io.File
import java.io.IOException

class SettingsFragment : PreferenceFragmentCompat() {

    private val dbOutputPath: Preference? by lazy {
        findPreference(RessourceFinder.getStringRessourceById(R.string.db_output_path))
    }
    private val FILE_CHOOOSER_REQUEST_RESTORE_COPY =
        PreferenceKeys.FILE_CHOOOSER_REQUEST_RESTORE_COPY
    private val FILE_CHOOOSER_REQUEST_SAFTY_COPY = PreferenceKeys.FILE_CHOOOSER_REQUEST_SAFTY_COPY
    private val timeSliderView: ListPreference? by lazy {
        findPreference(RessourceFinder.getStringRessourceById(R.string.pref_time_slider))
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
        val shareDb: Preference? =
            findPreference(RessourceFinder.getStringRessourceById(R.string.safty_copy))
        val restoreDbPath: Preference? =
            findPreference(RessourceFinder.getStringRessourceById(R.string.safty_copy))

        dbOutputPath?.setOnPreferenceClickListener {
            openFolderChooser()
            true
        }
        restoreDbPath?.setOnPreferenceClickListener {
            openFileChooser()
            true
        }
        shareDb?.setOnPreferenceClickListener {
            exportDb()
            true
        }

        //Fixme
        timeSliderView?.setOnPreferenceChangeListener { _, newValue ->
            val range = TimeRange.translate(newValue as String)
            AppPreferenceManager.setTimeSliderView(range)
            Log.d("jkfhjkwqhfjfhjhfakhk", AppPreferenceManager.getTimeSliderView().toString())
            timeSliderView!!.summary = newValue
            true
        }

        initPrefs()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_CHOOOSER_REQUEST_SAFTY_COPY && resultCode == RESULT_OK) {
            val selectedfile = data?.data!!
            val paths: List<String> = selectedfile.path!!.split(":")
            val completePath = Environment.getExternalStorageDirectory().toString() +
                    if (paths.size > 1) File.separator + paths[1] else ""
            AppPreferenceManager.setOutputPath(completePath)
            dbOutputPath!!.summary = completePath

        }
        if (requestCode == FILE_CHOOOSER_REQUEST_RESTORE_COPY && resultCode == RESULT_OK) {
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
            FILE_CHOOOSER_REQUEST_SAFTY_COPY
        )
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, FILE_CHOOOSER_REQUEST_RESTORE_COPY)
    }

    private fun exportDb() {
        try {
            AppFileProvider().exportDBtoPreferencePath()
            this.context?.let {
                setAlertWithoutContent(
                    it,
                    Alert.Builder().dialogType(SweetAlertDialog.SUCCESS_TYPE)
                        .title("Die Datenbank wurde exportiert !").build()
                )
            }
        } catch (e: IOException) {
            Log.e("exportDb", e.localizedMessage)
            this.context?.let {
                setAlertWithoutContent(
                    it,
                    Alert.Builder().dialogType(SweetAlertDialog.ERROR_TYPE)
                        .title(String.format("Der Export ist Schiefgelaufen %s", "\ud83d\ude13"))
                        .build()
                )
            }
        }
    }

    private fun restoreDB(path: Uri?) {
        try {
            if (path?.let { AppFileProvider().restoreDBfromPreferencePath(it) } == true) {
                SweetAlertDialog(this.context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Die Datenbank wurde wiederhergestellt !")
                    .setContentText("Die Anwendung wird neu gestartet.")
                    .setConfirmClickListener { sDialog: SweetAlertDialog? ->
                        val context: Context? = this.context
                        val packageManager: PackageManager = context!!.packageManager
                        val intent =
                            packageManager.getLaunchIntentForPackage(context!!.packageName)!!
                        val componentName = intent.component
                        val mainIntent = Intent.makeRestartActivityTask(componentName)
                        context.startActivity(mainIntent)
                        Runtime.getRuntime().exit(0)
                    }
                    .show()
            }
        } catch (e: IOException) {
            Log.d("restoreDb", e.localizedMessage as String)
            this.context?.let {
                setAlertWithoutContent(
                    it,
                    Alert.Builder().dialogType(SweetAlertDialog.ERROR_TYPE)
                        .title(String.format("Der Restore ist Schiefgelaufen %s", "\ud83d\ude13"))
                        .build()
                )
            }
        }
    }
}