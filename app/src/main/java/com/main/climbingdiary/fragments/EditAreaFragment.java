package com.main.climbingdiary.fragments;

import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.main.climbingdiary.R;
import com.main.climbingdiary.activities.SettingsActivity;
import com.main.climbingdiary.database.entities.Area;
import com.main.climbingdiary.database.entities.AreaRepository;

import java.util.ArrayList;

public class EditAreaFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsActivity.getSettingsActivity().setTheme(android.R.style.Theme_Black);
        Context context = getActivity();
        AreaRepository areaRepository = AreaRepository.getInstance();

        PreferenceScreen preferenceScreen = getPreferenceManager().createPreferenceScreen(context);
        setPreferenceScreen(preferenceScreen);

        //ToDo: Text farbe von weiß auf schwarz ändern !!"

        ArrayList<Area> areaArrayList = areaRepository.getAreaList();
        for (Area a : areaArrayList) {
            Preference editTextPreference = new Preference(context);
            editTextPreference.setKey(Integer.toString(a.getId()));
            editTextPreference.setTitle(a.getName());
            getPreferenceScreen().addPreference(editTextPreference);
        }

    }
}
