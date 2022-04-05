package com.main.climbingdiary.fragments

import android.R
import android.content.Context
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import com.main.climbingdiary.activities.SettingsActivity.Companion.getSettingsActivity
import com.main.climbingdiary.database.entities.AreaRepository

class EditAreaFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSettingsActivity().setTheme(R.style.Theme_Black)
        val context: Context = activity
        val areaRepository = AreaRepository
        val preferenceScreen = preferenceManager.createPreferenceScreen(context)
        setPreferenceScreen(preferenceScreen)

        //ToDo: Text farbe von weiß auf schwarz ändern !!"
        val areaArrayList = areaRepository.getAreaList()
        for ((_, name, id) in areaArrayList) {
            val editTextPreference = Preference(context)
            editTextPreference.key = id.toString()
            editTextPreference.title = name
            getPreferenceScreen().addPreference(editTextPreference)
        }
    }
}