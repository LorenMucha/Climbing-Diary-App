package com.main.climbingdiary.fragments;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.main.climbingdiary.R;
import com.main.climbingdiary.activities.MainActivity;
import com.main.climbingdiary.activities.SettingsActivity;
import com.main.climbingdiary.common.AlertManager;
import com.main.climbingdiary.common.AppFileProvider;
import com.main.climbingdiary.common.preferences.AppPreferenceManager;
import com.main.climbingdiary.common.preferences.PreferenceKeys;
import com.main.climbingdiary.models.Alert;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lombok.var;

import static android.app.Activity.RESULT_OK;
import static com.main.climbingdiary.common.preferences.PreferenceKeys.FILE_CHOOOSER_REQUEST_RESTORE_COPY;
import static com.main.climbingdiary.common.preferences.PreferenceKeys.FILE_CHOOOSER_REQUEST_SAFTY_COPY;

public class SettingsFragment extends PreferenceFragment {

    private Preference dbOutputPath;
    private static volatile SettingsFragment INSTANZ = null;

    public synchronized static SettingsFragment getInstance() {
        if (INSTANZ == null) {
            INSTANZ = new SettingsFragment();
        }
        return INSTANZ;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        PreferenceManager.getDefaultSharedPreferences(MainActivity.getMainAppContext());

        dbOutputPath = getPreferenceManager().findPreference(PreferenceKeys.DB_OUTPUT_PATH);
        Preference shareDb = getPreferenceManager().findPreference(PreferenceKeys.SAFTY_COPY);
        Preference restoreDbPath = getPreferenceManager().findPreference(PreferenceKeys.RESTORE_COPY);
        /* ToDo
        Preference changeAreaName = getPreferenceManager().findPreference(PreferenceKeys.UPDATE_AREA);
        Preference changeSectorName = getPreferenceManager().findPreference(PreferenceKeys.UPDATE_SECTOR);*/

        dbOutputPath.setOnPreferenceClickListener(preference -> {
            this.openFolderChooser();
            return true;
        });
        assert restoreDbPath != null;
        restoreDbPath.setOnPreferenceClickListener(preference -> {
            this.openFileChooser();
            return true;
        });
        assert shareDb != null;
        shareDb.setOnPreferenceClickListener(preference -> {
            this.exportDb();
            return true;
        });
       /* ToDo
       assert changeAreaName != null;
        changeAreaName.setOnPreferenceClickListener(preference -> {
            this.changeAreaOrSectorName(changeAreaName.getKey());
            return true;
        });
        assert changeSectorName != null;
        changeSectorName.setOnPreferenceClickListener(preference -> {
            this.changeAreaOrSectorName(changeSectorName.getKey());
            return true;
        });*/
        this.initPrefs();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOOSER_REQUEST_SAFTY_COPY && resultCode == RESULT_OK) {
            Uri selectedfile = data.getData();
            assert selectedfile != null;
            AppPreferenceManager.setOutputPath(selectedfile.toString());
            dbOutputPath.setSummary(AppPreferenceManager.getOutputPath());
        }
        if (requestCode == FILE_CHOOOSER_REQUEST_RESTORE_COPY && resultCode == RESULT_OK) {
            Uri path = data.getData();
            this.restoreDB(path);
        }
    }

    private void initPrefs() {
        try {
            dbOutputPath.setSummary(AppPreferenceManager.getOutputPath());
        } catch (NullPointerException ignored) {
        }
    }

    private void openFolderChooser() {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(Intent.createChooser(i, "Output-Pfad"), FILE_CHOOOSER_REQUEST_SAFTY_COPY);
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, FILE_CHOOOSER_REQUEST_RESTORE_COPY);
    }

    private void exportDb() {
        try {
            new AppFileProvider().exportDBtoPreferencePath();
            new AlertManager().setAlertWithoutContent(
                    this.getContext(),
                    Alert.builder().dialogType(SweetAlertDialog.SUCCESS_TYPE).title("Die Datenbank wurde exportiert !").build());
        } catch (IOException e) {
            Log.e("exportDb", e.getLocalizedMessage());
            new AlertManager().setAlertWithoutContent(
                    this.getContext(),
                    Alert.builder().dialogType(SweetAlertDialog.ERROR_TYPE)
                            .title(String.format("Der Export ist Schiefgelaufen %s", "\ud83d\ude13"))
                            .build());
        }
    }

    private void restoreDB(Uri path) {
        try {
            if (new AppFileProvider().restoreDBfromPreferencePath(path)) {
                new SweetAlertDialog(this.getContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Die Datenbank wurde wiederhergestellt !")
                        .setContentText("Die Anwendung wird neu gestartet.")
                        .setConfirmClickListener(sDialog -> {
                            var context = this.getContext();
                            PackageManager packageManager = context.getPackageManager();
                            Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
                            assert intent != null;
                            ComponentName componentName = intent.getComponent();
                            Intent mainIntent = Intent.makeRestartActivityTask(componentName);
                            context.startActivity(mainIntent);
                            Runtime.getRuntime().exit(0);
                        })
                        .show();
            }
        } catch (IOException e) {
            Log.d("restoreDb", e.getLocalizedMessage());
            new AlertManager().setAlertWithoutContent(
                    this.getContext(),
                    Alert.builder().dialogType(SweetAlertDialog.ERROR_TYPE)
                            .title(String.format("Der Restore ist Schiefgelaufen %s", "\ud83d\ude13"))
                            .build());
        }
    }

    private void changeAreaOrSectorName(String key) {
        SettingsActivity
                .getSettingsActivity()
                .getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new EditAreaFragment()).commit();

    }
}