package com.main.climbingdiary.common.preferences;

import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.main.climbingdiary.activities.MainActivity;
import com.main.climbingdiary.common.FileUtil;

public class AppPreferenceManager {

    private static SharedPreferences.Editor EDITOR;
    private static SharedPreferences PREFS = PreferenceManager.getDefaultSharedPreferences(MainActivity.getMainAppContext());

    public static String getOutputPath() {
        return PREFS.getString(PreferenceKeys.DB_OUTPUT_PATH, "not set");
    }

    public static void setOutputPath(String path) {
        Uri treeUri = Uri.parse(path);
        String pathSet = FileUtil.getFullPathFromTreeUri(treeUri, MainActivity.getMainAppContext());
        EDITOR = PREFS.edit();
        EDITOR.putString(PreferenceKeys.DB_OUTPUT_PATH, pathSet);
        EDITOR.apply();
    }

    public static void setOutputDbPath(String db) {

    }
}
