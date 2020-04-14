package com.main.climbingdiary.common;

import android.Manifest;

import com.main.climbingdiary.activities.MainActivity;

import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class AppPermissions {
    public static final int RC_READ_AND_WRITE = 123;
    public static String[] PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.LOCATION_HARDWARE,
            Manifest.permission.ACCESS_NETWORK_STATE};

    public static void checkPermissions(){
        if (!EasyPermissions.hasPermissions(MainActivity.getMainAppContext(), PERMS)) {
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(MainActivity.getMainActivity(), RC_READ_AND_WRITE, PERMS).build());
        }
    }
}
