package com.main.climbingdiary.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.jakewharton.processphoenix.ProcessPhoenix;
import com.main.climbingdiary.R;
import com.main.climbingdiary.activities.MainActivity;
import com.main.climbingdiary.common.AppFileProvider;
import com.main.climbingdiary.common.EnvironmentParamter;
import com.main.climbingdiary.common.preferences.AppPreferenceManager;
import com.main.climbingdiary.common.preferences.PreferenceKeys;

import java.io.File;
import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_OK;
import static com.main.climbingdiary.common.EnvironmentParamter.dbExportName;

public class SettingsFragment extends PreferenceFragment{

    private Preference dbOutputPath, restoreDbPath,shareDb;
    private static int FILE_CHOOOSER_REQUEST_SAFTY_COPY = 12345;
    private static int FILE_CHOOOSER_REQUEST_RESTORE_COPY = 123456;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        PreferenceManager.getDefaultSharedPreferences(MainActivity.getMainAppContext());

        dbOutputPath = getPreferenceManager().findPreference(PreferenceKeys.DB_OUTPUT_PATH);
        shareDb = getPreferenceManager().findPreference(PreferenceKeys.SAFTY_COPY);
        restoreDbPath = getPreferenceManager().findPreference(PreferenceKeys.RESTORE_COPY);
        dbOutputPath.setOnPreferenceClickListener(preference -> {
            this.openFolderChooser();
            return true;
        });
        restoreDbPath.setOnPreferenceClickListener(preference -> {
            this.openFileChooser();
            return true;
        });
        shareDb.setOnPreferenceClickListener(preference -> {
            this.exportDb();
            return true;
        });
        this.initPrefs();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FILE_CHOOOSER_REQUEST_SAFTY_COPY && resultCode == RESULT_OK) {
            Uri selectedfile = data.getData();
            assert selectedfile != null;
            AppPreferenceManager.setOutputPath(selectedfile.toString());
            dbOutputPath.setSummary(AppPreferenceManager.getOutputPath());
        }
        if(requestCode == FILE_CHOOOSER_REQUEST_RESTORE_COPY && resultCode == RESULT_OK) {
            Uri path = data.getData();
            this.restoreDB(path);
        }
    }

    private void initPrefs(){
        try {
            dbOutputPath.setSummary(AppPreferenceManager.getOutputPath());
        }catch(NullPointerException ignored){}
    }

    private void openFolderChooser(){
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(Intent.createChooser(i, "Output-Pfad"), FILE_CHOOOSER_REQUEST_SAFTY_COPY);
    }

    private void openFileChooser(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, FILE_CHOOOSER_REQUEST_RESTORE_COPY);
    }

    private void exportDb(){
        try {
             new AppFileProvider().exportDBtoPreferencePath();
             new SweetAlertDialog(this.getContext(),SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Die Datenbank wurde exportiert !")
                    .show();
        } catch (IOException e) {
            Log.e("exportDb",e.getLocalizedMessage());
            new SweetAlertDialog(this.getContext(),SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(String.format("Der Export ist Schiefgelaufen %s","\ud83d\ude13"))
                    .show();
        }
    }

    private void restoreDB(Uri path){
        try{
            if(new AppFileProvider().restoreDBfromPreferencePath(path)){
                new SweetAlertDialog(this.getContext(),SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Die Datenbank wurde wiederhergestellt !")
                        .setContentText("Die Anwendung wird neu gestartet.")
                        .setConfirmClickListener(sDialog -> ProcessPhoenix.triggerRebirth(MainActivity.getMainAppContext()))
                        .show();
            }
        }catch(IOException e){
            Log.d("restoreDb",e.getLocalizedMessage());
            new SweetAlertDialog(this.getContext(),SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(String.format("Der Restore ist Schiefgelaufen %s","\ud83d\ude13"))
                    .show();
        }
    }
}