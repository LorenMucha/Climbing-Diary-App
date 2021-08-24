package com.main.climbingdiary.fragments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.preference.Preference
import android.preference.Preference.OnPreferenceClickListener
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.util.Log
import cn.pedant.SweetAlert.SweetAlertDialog
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity.Companion.getMainAppContext
import com.main.climbingdiary.common.AlertManager.setAlertWithoutContent
import com.main.climbingdiary.common.AppFileProvider
import com.main.climbingdiary.common.AppPermissions
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.common.preferences.AppPreferenceManager.getOutputPath
import com.main.climbingdiary.common.preferences.PreferenceKeys
import com.main.climbingdiary.common.preferences.PreferenceKeys.DB_OUTPUT_PATH
import com.main.climbingdiary.common.preferences.PreferenceKeys.RESTORE_COPY
import com.main.climbingdiary.common.preferences.PreferenceKeys.SAFTY_COPY
import com.main.climbingdiary.models.Alert
import java.io.File
import java.io.IOException
import java.util.*


@SuppressLint("ValidFragment", "StaticFieldLeak")
object SettingsFragment: PreferenceFragment() {

    private val dbOutputPath: Preference? by lazy{ preferenceManager.findPreference(DB_OUTPUT_PATH)}
    private val FILE_CHOOOSER_REQUEST_RESTORE_COPY =
        PreferenceKeys.FILE_CHOOOSER_REQUEST_RESTORE_COPY
    private val FILE_CHOOOSER_REQUEST_SAFTY_COPY = PreferenceKeys.FILE_CHOOOSER_REQUEST_SAFTY_COPY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
        PreferenceManager.getDefaultSharedPreferences(getMainAppContext())
        val shareDb = preferenceManager.findPreference(SAFTY_COPY)
        val restoreDbPath = preferenceManager.findPreference(RESTORE_COPY)
        /* ToDo
        Preference changeAreaName = getPreferenceManager().findPreference(PreferenceKeys.UPDATE_AREA);
        Preference changeSectorName = getPreferenceManager().findPreference(PreferenceKeys.UPDATE_SECTOR);*/
        dbOutputPath!!.onPreferenceClickListener = OnPreferenceClickListener {
            openFolderChooser()
            true
        }
        restoreDbPath!!.onPreferenceClickListener =
            OnPreferenceClickListener {
                openFileChooser()
                true
            }
        shareDb!!.onPreferenceClickListener = OnPreferenceClickListener {
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
        });*/
        initPrefs()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_CHOOOSER_REQUEST_SAFTY_COPY && resultCode == RESULT_OK) {
            val selectedfile = data.data!!
            val paths: List<String> = selectedfile.path!!.split(":")
            val completePath = Environment.getExternalStorageDirectory().toString() +
                    if (paths.size > 1) File.separator + paths[1] else ""
            AppPreferenceManager.setOutputPath(completePath)
            dbOutputPath!!.summary = completePath

        }
        if (requestCode == FILE_CHOOOSER_REQUEST_RESTORE_COPY && resultCode == RESULT_OK) {
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
            setAlertWithoutContent(
                this.context,
                Alert.Builder().dialogType(SweetAlertDialog.SUCCESS_TYPE)
                    .title("Die Datenbank wurde exportiert !").build()
            )
        } catch (e: IOException) {
            Log.e("exportDb", e.localizedMessage)
            setAlertWithoutContent(
                this.context,
                Alert.Builder().dialogType(SweetAlertDialog.ERROR_TYPE)
                    .title(String.format("Der Export ist Schiefgelaufen %s", "\ud83d\ude13"))
                    .build()
            )
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
            Log.d("restoreDb", e.localizedMessage)
            setAlertWithoutContent(
                this.context,
                Alert.Builder().dialogType(SweetAlertDialog.ERROR_TYPE)
                    .title(String.format("Der Restore ist Schiefgelaufen %s", "\ud83d\ude13"))
                    .build()
            )
        }
    }
}