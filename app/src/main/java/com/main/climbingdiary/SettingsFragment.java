package com.main.climbingdiary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.main.climbingdiary.environment.EnvironmentManager;
import com.main.climbingdiary.environment.preferences.AppPreferenceManager;
import com.main.climbingdiary.environment.preferences.PreferenceKeys;

import java.io.File;

import static android.app.Activity.RESULT_OK;

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
            this.shareDb();
            return true;
        });
        this.initPrefs();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FILE_CHOOOSER_REQUEST && resultCode == RESULT_OK) {
            Uri selectedfile = data.getData();
            AppPreferenceManager.setOutputPath(selectedfile.getPath());
            dbOutputPath.setSummary(selectedfile.toString());
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
    //Fixme
    private void shareDb(){
        String path = EnvironmentManager.getDbPath();
        Log.d("SHARE PREFERENCE+++++",path);
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        File fileWithinMyDir = new File(path);

        if(fileWithinMyDir.exists()) {
            intentShareFile.setType("application/pdf");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+path));

            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "Sharing File...");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

            startActivity(Intent.createChooser(intentShareFile, "Share File"));
        }
    }
}