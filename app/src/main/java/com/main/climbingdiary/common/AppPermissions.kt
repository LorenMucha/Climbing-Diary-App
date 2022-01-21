package com.main.climbingdiary.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Environment
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.main.climbingdiary.BuildConfig
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.models.Alert
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

object AppPermissions {

    var PERMS = mapOf(
        122 to Manifest.permission.WRITE_EXTERNAL_STORAGE,
        123 to Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    fun checkPermissions(activity: Activity) {
        for ((key, value) in PERMS) {
            if (ContextCompat.checkSelfPermission(
                    activity.applicationContext,
                    value
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(activity, arrayOf(value), key)
            }
        }

    }

    fun checkFileManagementPermission(context: Context?) {
        val infoAlert = AlertFactory.getAlert(
            context!!,
            Alert(
                title = "Fehlende Berechtigungung zum Export der Datenbank",
                message="Bitte Berechtigung erteilen und Export neu auslösen !",
                image = R.drawable.sad,
                dialogType = SweetAlertDialog.WARNING_TYPE
            )
        )

        if (SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            infoAlert.setConfirmClickListener {
                val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")

                MainActivity.getMainActivity().startActivity(
                    Intent(
                        Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                        uri
                    )
                )
                it.cancel()
            }.show()
        }
    }
}