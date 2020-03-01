package com.main.climbingdiary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.content.FileProvider;
import android.util.Log;

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

    private Preference dbOutputPath;
    private Preference shareDb;
    private static int FILE_CHOOOSER_REQUEST = 12345;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        PreferenceManager.getDefaultSharedPreferences(MainActivity.getMainAppContext());

        dbOutputPath = getPreferenceManager().findPreference(PreferenceKeys.DB_OUTPUT_PATH);
        shareDb = getPreferenceManager().findPreference(PreferenceKeys.SAFTY_COPY);
        dbOutputPath.setOnPreferenceClickListener(preference -> {
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
        if(requestCode == FILE_CHOOOSER_REQUEST && resultCode == RESULT_OK) {
            Uri selectedfile = data.getData();
            assert selectedfile != null;
            AppPreferenceManager.setOutputPath(selectedfile.toString());
            dbOutputPath.setSummary(AppPreferenceManager.getOutputPath());
        }
    }

    private void initPrefs(){
        try {
            dbOutputPath.setSummary(AppPreferenceManager.getOutputPath());
        }catch(NullPointerException ex){}
    }

    private void openFileChooser(){
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(Intent.createChooser(i, "Output-Pfad"), FILE_CHOOOSER_REQUEST);
    }

    private void exportDb(){
        try {
             new AppFileProvider().exportDBtoPreferencePath();
             new SweetAlertDialog(this.getContext(),SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Die Datenbank wurde exportiert !")
                    .setContentText("Sie liegt im Verzeichnis: ")
                     .setNeutralButton("Teilen", sweetAlertDialog -> {
                         Intent intent = new Intent(Intent.ACTION_SEND);
                         intent.setType("application/octet-stream");
                         File dbToShare = new File(AppPreferenceManager.getOutputPath(), dbExportName);
                         String packageName = EnvironmentParamter.PACKAGE_NAME;
                         Uri uri = FileProvider.getUriForFile(MainActivity.getMainAppContext(),
                                 "com.main.climbingdiary.StatisticFragment",
                                 dbToShare);

                         intent.putExtra(Intent.EXTRA_STREAM, uri);

                         startActivity(Intent.createChooser(intent, "Backup via:"));
                         sweetAlertDialog.cancel();
                     })
                    .show();
        } catch (IOException e) {
            Log.e("exportDb",e.getLocalizedMessage());
            new SweetAlertDialog(this.getContext(),SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(String.format("Der Export ist Schiefgelaufen %s","\ud83d\ude13"))
                    .show();
        }
    }
}