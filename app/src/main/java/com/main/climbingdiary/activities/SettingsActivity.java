package com.main.climbingdiary.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.main.climbingdiary.common.AppPermissions;
import com.main.climbingdiary.fragments.SettingsFragment;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static AppCompatActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsActivity.mainActivity = this;
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppPermissions.checkPermissions();
    }

    public static AppCompatActivity getSettingsActivity(){return SettingsActivity.mainActivity;}

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}