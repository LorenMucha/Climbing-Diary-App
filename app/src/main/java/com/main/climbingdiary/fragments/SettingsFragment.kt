package com.main.climbingdiary.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceFragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.activities.SettingsActivity
import com.main.climbingdiary.common.AlertManager
import com.main.climbingdiary.common.AppFileProvider
import com.main.climbingdiary.common.preferences.AppPreferenceManager.getOutputPath
import com.main.climbingdiary.common.preferences.AppPreferenceManager.setOutputPath
import com.main.climbingdiary.common.preferences.PreferenceKeys
import com.main.climbingdiary.common.preferences.PreferenceKeys.FILE_CHOOOSER_REQUEST_RESTORE_COPY
import com.main.climbingdiary.common.preferences.PreferenceKeys.FILE_CHOOOSER_REQUEST_SAFTY_COPY
import com.main.climbingdiary.model.Alert
import java.io.IOException



@SuppressLint("ValidFragment", "StaticFieldLeak")
object SettingsFragment: PreferenceFragment() {

    private var dbOutputPath: Preference? = preferenceManager.findPreference(PreferenceKeys.DB_OUTPUT_PATH)

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
        PreferenceManager.getDefaultSharedPreferences(MainActivity.getAppContext())
        dbOutputPath = preferenceManager.findPreference(PreferenceKeys.DB_OUTPUT_PATH)
        val shareDb: Preference? = preferenceManager.findPreference(PreferenceKeys.SAFTY_COPY)
        val restoreDbPath: Preference? =
            preferenceManager.findPreference(PreferenceKeys.RESTORE_COPY)
        /* ToDo
        Preference changeAreaName = getPreferenceManager().findPreference(PreferenceKeys.UPDATE_AREA);
        Preference changeSectorName = getPreferenceManager().findPreference(PreferenceKeys.UPDATE_SECTOR);*/
        dbOutputPath!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            openFolderChooser()
            true
        }
        assert(restoreDbPath != null)
        restoreDbPath!!.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                openFileChooser()
                true
            }
        assert(shareDb != null)
        shareDb!!.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                exportDb()
                true
            }
        /* ToDo
       assert changeAreaName != null;
        changeAreaName.setOnPreferenceClickListener(preference -> {
            this.changeAreaOrSectorName(changeAreaName.getKey());
            return true;
        });
        assert changeSectorName != null;
        changeSectorName.setOnPreferenceClickListener(preference -> {
            this.changeAreaOrSectorName(changeSectorName.getKey());
            return true;
        });*/initPrefs()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_CHOOOSER_REQUEST_SAFTY_COPY && resultCode == Activity.RESULT_OK) {
            val selectedfile = data.data!!
            setOutputPath(selectedfile.toString())
            dbOutputPath!!.summary = getOutputPath()
        }
        if (requestCode == FILE_CHOOOSER_REQUEST_RESTORE_COPY && resultCode == Activity.RESULT_OK) {
            val path = data.data
            restoreDB(path)
        }
    }

    private fun initPrefs() {
        try {
            dbOutputPath!!.summary = getOutputPath()
        } catch (ignored: NullPointerException) {
        }
    }

    private fun openFolderChooser() {
        val i = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        i.addCategory(Intent.CATEGORY_DEFAULT)
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
            AlertManager.setAlertWithoutContent(
                this.context,
                Alert("Die Datenbank wurde exportiert !","",SweetAlertDialog.SUCCESS_TYPE)
            )
        } catch (e: IOException) {
            Log.e("exportDb", e.localizedMessage)
            AlertManager.setAlertWithoutContent(
                this.context,
                Alert(String.format("Der Export ist Schiefgelaufen %s", "\ud83d\ude13"),"",SweetAlertDialog.ERROR_TYPE)
            )
        }
    }

    private fun restoreDB(path: Uri?) {
        try {
            if (AppFileProvider().restoreDBfromPreferencePath(path!!)) {
                SweetAlertDialog(this.context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Die Datenbank wurde wiederhergestellt !")
                    .setContentText("Die Anwendung wird neu gestartet.")
                    .setConfirmClickListener { sDialog: SweetAlertDialog? ->
                        val context = this.context
                        val packageManager = context.packageManager
                        val intent =
                            packageManager.getLaunchIntentForPackage(context.packageName)!!
                        val componentName = intent.component
                        val mainIntent = Intent.makeRestartActivityTask(componentName)
                        context.startActivity(mainIntent)
                        Runtime.getRuntime().exit(0)
                    }
                    .show()
            }
        } catch (e: IOException) {
            Log.d("restoreDb", e.localizedMessage)
            AlertManager.setAlertWithoutContent(
                this.context,
                Alert(String.format("Der Restore ist Schiefgelaufen %s", "\ud83d\ude13"),"",SweetAlertDialog.ERROR_TYPE)            )
        }
    }
}