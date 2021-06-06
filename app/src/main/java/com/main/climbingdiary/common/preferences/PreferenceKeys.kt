package com.main.climbingdiary.common.preferences

import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity

object PreferenceKeys {
    const val preferences = "prefs"
    const val FILE_CHOOOSER_REQUEST_SAFTY_COPY = 12345
    const val FILE_CHOOOSER_REQUEST_RESTORE_COPY = 123456
    var DB_OUTPUT_PATH: String = MainActivity.getAppContext()!!.getString(R.string.db_output_path)
    var SAFTY_COPY: String = MainActivity.getAppContext()!!.getString(R.string.safty_copy)
    var RESTORE_COPY: String = MainActivity.getAppContext()!!.getString(R.string.safty_restore)
    var UPDATE_AREA: String = MainActivity.getAppContext()!!.getString(R.string.pref_gebiete)
    var AREA_LIST: String = MainActivity.getAppContext()!!.getString(R.string.pref_gebiete_list)
    var UPDATE_SECTOR: String = MainActivity.getAppContext()!!.getString(R.string.pref_sektoren)
    const val SPORT = "sport_type"
    const val FILTER = "filter"
    const val FILTER_MENU = "filter_menu"
    const val SORT = "sort"
    const val TAB = "tab"
    val TEMP_KEYS = arrayOf(FILTER, FILTER_MENU, SORT, TAB, SPORT)
    val TEMP_FILTER = arrayOf(FILTER, FILTER_MENU)

}