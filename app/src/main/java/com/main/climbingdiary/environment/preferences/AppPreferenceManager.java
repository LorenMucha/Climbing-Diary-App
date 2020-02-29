package com.main.climbingdiary.environment.preferences;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.main.climbingdiary.MainActivity;

public class AppPreferenceManager {

    private static SharedPreferences.Editor EDITOR;
    private static SharedPreferences PREFS = PreferenceManager.getDefaultSharedPreferences(MainActivity.getMainAppContext());


    public static void setOutputPath(String path){
        EDITOR = PREFS.edit();
        EDITOR.putString(PreferenceKeys.DB_OUTPUT_PATH,path);
        EDITOR.apply();
    }
    public static String getOutputPath(){
        return PREFS.getString(PreferenceKeys.DB_OUTPUT_PATH,"not set");
    }
}
