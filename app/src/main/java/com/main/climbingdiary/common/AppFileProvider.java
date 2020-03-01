package com.main.climbingdiary.common;

import android.Manifest;
import android.os.Environment;

import com.main.climbingdiary.MainActivity;
import com.main.climbingdiary.R;
import com.main.climbingdiary.common.preferences.AppPreferenceManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

import static android.support.v4.content.res.TypedArrayUtils.getString;
import static com.main.climbingdiary.common.EnvironmentParamter.dbExportName;

public class AppFileProvider {

    private static final int RC_READ_AND_WRITE = 123;
    String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @AfterPermissionGranted(RC_READ_AND_WRITE)
    public void exportDBtoPreferencePath() throws IOException {
        if (EasyPermissions.hasPermissions(MainActivity.getMainAppContext(), perms)) {
            String dbName = EnvironmentParamter.DB_NAME;
            File data = Environment.getDataDirectory();
            String path = AppPreferenceManager.getOutputPath();
            FileChannel source;
            FileChannel destination;
            String currentDBPath = "/data/" + EnvironmentParamter.PACKAGE_NAME + "/databases/" + dbName;
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(path, dbExportName);
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB, true).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
        }else {
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(MainActivity.getMainActivity(), RC_READ_AND_WRITE, perms).build());
        }
    }
}
