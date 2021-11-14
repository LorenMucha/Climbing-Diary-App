package com.main.climbingdiary.common.preferences

import androidx.preference.PreferenceManager
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.models.*
import com.main.climbingdiary.models.Tabs.Companion.stringToTabs

object AppPreferenceManager {
    private val PREFS =
        PreferenceManager.getDefaultSharedPreferences(MainActivity.getMainAppContext())
    private val EDITOR = PREFS.edit()

    fun getOutputPath(): String? {
        return PREFS.getString(PreferenceKeys.DB_OUTPUT_PATH, "not set")
    }

    fun setOutputPath(path: String?) {
        EDITOR.putString(PreferenceKeys.DB_OUTPUT_PATH, path)
        EDITOR.apply()
    }

    fun getSportType(): SportType {
        return SportType.stringToSportType(
            PREFS.getString(
                PreferenceKeys.SPORT,
                "klettern"
            )
        )
    }

    fun setSportType(type: SportType) {
        val value = type.typeToString()
        EDITOR.putString(PreferenceKeys.SPORT, value)
        EDITOR.apply()
    }

    fun getFilter(): String? {
        return PREFS.getString(PreferenceKeys.FILTER, "")
    }

    fun setFilter(filter: String?) {
        EDITOR.putString(PreferenceKeys.FILTER, filter)
        EDITOR.apply()
    }

    fun getSort(): RouteSort? {
        return RouteSort.stringToSportType(
            PREFS.getString(
                PreferenceKeys.SORT,
                "date"
            )
        )
    }

    fun setSort(sort: RouteSort) {
        EDITOR.putString(PreferenceKeys.SORT, sort.typeToString())
        EDITOR.apply()
    }

    fun getFilterSetter(): MenuValues {
        return MenuValues.stringToSportType(
                PREFS.getString(
                    PreferenceKeys.FILTER_MENU,
                    "date"
                )
        )
    }

    fun setFilterSetter(value: MenuValues) {
        EDITOR.putString(PreferenceKeys.FILTER_MENU, value.typeToString())
        EDITOR.apply()
    }

    fun removeAllFilterPrefs() {
        PREFS.edit().remove(PreferenceKeys.FILTER).apply()
        PREFS.edit().remove(PreferenceKeys.FILTER_MENU).apply()
    }

    fun getSelectedTabsTitle(): Tabs? {
        return stringToTabs(PREFS.getString(PreferenceKeys.TAB, ""))
    }

    fun setSelectedTabsTitle(tab: Tabs) {
        EDITOR.putString(PreferenceKeys.TAB, tab.typeToString())
        EDITOR.apply()
    }

    fun setTimeSliderView(range: TimeRange?){
        EDITOR.putString(PreferenceKeys.TIME_SLIDER_RANGE, range.toString())
        EDITOR.apply()
    }

    fun getTimeSliderView(): TimeRange{
        val value : String? = PREFS.getString(PreferenceKeys.TIME_SLIDER_RANGE, TimeRange.YEAR.toString())
        return TimeRange.valueOf(value!!)
    }
}