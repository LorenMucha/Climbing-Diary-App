package com.main.climbingdiary.common;

import android.net.Uri;
import android.os.Environment;

import com.main.climbingdiary.activities.MainActivity;
import com.main.climbingdiary.common.preferences.AppPreferenceManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import static com.main.climbingdiary.common.EnvironmentParamter.dbExportName;

public class AppFileProvider {

    public void exportDBtoPreferencePath() throws IOException {
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
    }

    public boolean restoreDBfromPreferencePath(Uri uri) throws IOException{
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
        return false;
    }

}
