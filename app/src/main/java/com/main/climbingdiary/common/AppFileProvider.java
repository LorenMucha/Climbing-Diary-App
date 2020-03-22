package com.main.climbingdiary.common;

import android.Manifest;
import android.net.Uri;
import android.os.Environment;

import com.main.climbingdiary.MainActivity;
import com.main.climbingdiary.common.preferences.AppPreferenceManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

import static com.main.climbingdiary.common.EnvironmentParamter.dbExportName;

public class AppFileProvider {

    private static final int RC_READ_AND_WRITE = 123;
    private static String[] PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @AfterPermissionGranted(RC_READ_AND_WRITE)
    public void exportDBtoPreferencePath() throws IOException {
        if (EasyPermissions.hasPermissions(MainActivity.getMainAppContext(), PERMS)) {
            String path = AppPreferenceManager.getOutputPath();
            FileChannel source;
            FileChannel destination;
            File currentDB = new File(Environment.getDataDirectory(), EnvironmentParamter.DB_PATH);
            File backupDB = new File(path, dbExportName);
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB, true).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
        }else{
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(MainActivity.getMainActivity(), RC_READ_AND_WRITE, PERMS).build());
        }
    }

    @AfterPermissionGranted(RC_READ_AND_WRITE)
    public boolean restoreDBfromPreferencePath(Uri uri) throws IOException{
        if (EasyPermissions.hasPermissions(MainActivity.getMainAppContext(), PERMS)) {
           File dst = new File(Environment.getDataDirectory(), EnvironmentParamter.DB_PATH);
           if(dst.delete()) {
               InputStream in = MainActivity.getMainAppContext().getContentResolver().openInputStream(uri);
               OutputStream out = new FileOutputStream(dst, false);
               byte[] buf = new byte[1024];
               int len;
               assert in != null;
               while ((len = in.read(buf)) > 0) {
                   out.write(buf, 0, len);
               }
               out.flush();
               in.close();
               out.close();
               return true;
           }
        }else{
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(MainActivity.getMainActivity(), RC_READ_AND_WRITE, PERMS).build());
        }
        return false;
    }

}
