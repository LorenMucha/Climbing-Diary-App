package com.main.climbingdiary.common

import android.Manifest
import android.content.Context
import com.main.climbingdiary.activities.MainActivity
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

object AppPermissions {
    private const val RC_READ_AND_WRITE = 122
    var PERMS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @JvmStatic
    fun checkPermissions(context: Context) {
        for (perm in PERMS) {
            if (!EasyPermissions.hasPermissions(context, perm)) {
                // Request one permission
                EasyPermissions.requestPermissions(
                    PermissionRequest.Builder(
                        MainActivity.getMainActivity(),
                        RC_READ_AND_WRITE,
                        perm
                    ).build()
                )
            }
        }
    }
}