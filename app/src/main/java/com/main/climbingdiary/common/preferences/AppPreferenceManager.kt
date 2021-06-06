package com.main.climbingdiary.common.preferences

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.Uri
import com.main.climbingdiary.common.FileUtil
import com.main.climbingdiary.model.MenuValues
import com.main.climbingdiary.model.RouteSort
import com.main.climbingdiary.model.SportType
import com.main.climbingdiary.model.Tabs
import java.util.*


@SuppressLint("StaticFieldLeak")
object AppPreferenceManager {

    private lateinit var context: Context
    private lateinit var preferences: SharedPreferences

    operator fun invoke(context: Context): AppPreferenceManager {
        AppPreferenceManager.context = context
        preferences = context.getSharedPreferences(PreferenceKeys.preferences, MODE_PRIVATE)
        return this
    }

    fun setOutputPath(path: String) {
        val treeUri = Uri.parse(path)
        val pathSet: String? =
            FileUtil.getFullPathFromTreeUri(treeUri, context)
        preferences.edit().putString(PreferenceKeys.DB_OUTPUT_PATH, pathSet).apply()
    }

    fun getOutputPath(): String? {
        return preferences.getString(PreferenceKeys.DB_OUTPUT_PATH, "not set")
    }

    fun setSportType(type: SportType) {
        preferences.edit().putString(PreferenceKeys.SORT, type.toString()).apply()
    }

    fun getSportType(): SportType {
        return SportType.valueOf(
            preferences.getString(PreferenceKeys.SORT, "klettern").toString()
                .toUpperCase(Locale.ROOT)
        )
    }

    fun setFilter(filter: String?) {
        preferences.edit().putString(PreferenceKeys.FILTER, filter).apply()
    }

    fun getFilter(): String {
        return preferences.getString(PreferenceKeys.FILTER, "").toString()
    }

    fun getSort(): RouteSort {
        return RouteSort.valueOf(
            preferences.getString(PreferenceKeys.SORT, "date").toString().toUpperCase(Locale.ROOT)
        )
    }

    fun setSort(sort: RouteSort) {
        preferences.edit().putString(PreferenceKeys.SORT, sort.toString()).apply()
    }

    fun setFilterSetter(value: MenuValues) {
        preferences.edit().putString(PreferenceKeys.FILTER_MENU, value.toString()).apply()
    }

    fun getFilterSetter(): MenuValues {
        return MenuValues.valueOf(
            preferences.getString(PreferenceKeys.FILTER_MENU, "date").toString()
                .toUpperCase(Locale.ROOT)
        )
    }

    fun removeAllFilterPrefs() {
        preferences.edit().run {
            for (s in PreferenceKeys.TEMP_FILTER) {
                remove(s).apply()
            }
        }
    }

    fun removeAllTempPrefs() {
        preferences.edit().run {
            for (s in PreferenceKeys.TEMP_KEYS) {
                remove(s).apply()
            }
        }
    }

    fun setSelectedTabsTitle(tab: Tabs) {
        preferences.edit().putString(PreferenceKeys.TAB, tab.toString()).apply();
    }

    fun getSelectedTabsTitle(): Tabs {
        return Tabs.valueOf(
            preferences.getString(PreferenceKeys.TAB, "").toString().toUpperCase(Locale.ROOT)
        )
    }
}