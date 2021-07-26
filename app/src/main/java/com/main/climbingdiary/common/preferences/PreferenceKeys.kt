package com.main.climbingdiary.common.preferences

import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity

object PreferenceKeys {
    val FILE_CHOOOSER_REQUEST_SAFTY_COPY = 12345
    val FILE_CHOOOSER_REQUEST_RESTORE_COPY = 123456
    var DB_OUTPUT_PATH: String = MainActivity.getMainActivity().getString(R.string.db_output_path)
    var SAFTY_COPY = MainActivity.getMainActivity().getString(R.string.safty_copy)
    var RESTORE_COPY = MainActivity.getMainActivity().getString(R.string.safty_restore)
    var UPDATE_AREA = MainActivity.getMainActivity().getString(R.string.pref_gebiete)
    var AREA_LIST = MainActivity.getMainActivity().getString(R.string.pref_gebiete_list)
    var UPDATE_SECTOR = MainActivity.getMainActivity().getString(R.string.pref_sektoren)
    var SPORT = "sport_type"
    var FILTER = "filter"
    var FILTER_MENU = "filter_menu"
    var SORT = "sort"
    var TAB = "tab"
}