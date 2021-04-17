package com.main.climbingdiary.common.preferences;

import com.main.climbingdiary.R;
import com.main.climbingdiary.activities.MainActivity;

public class PreferenceKeys {
    public static final int FILE_CHOOOSER_REQUEST_SAFTY_COPY = 12345;
    public static final int FILE_CHOOOSER_REQUEST_RESTORE_COPY = 123456;
    public static String DB_OUTPUT_PATH = MainActivity.getMainActivity().getString(R.string.db_output_path);
    public static String SAFTY_COPY = MainActivity.getMainActivity().getString(R.string.safty_copy);
    public static String RESTORE_COPY = MainActivity.getMainActivity().getString(R.string.safty_restore);
    public static String UPDATE_AREA = MainActivity.getMainActivity().getString(R.string.pref_gebiete);
    public static String AREA_LIST = MainActivity.getMainActivity().getString(R.string.pref_gebiete_list);
    public static String UPDATE_SECTOR = MainActivity.getMainActivity().getString(R.string.pref_sektoren);
    public static String SPORT = "sport_type";
    public static String FILTER = "filter";
    public static String FILTER_MENU = "filter_menu";
    public static String SORT = "sort";
    public static String TAB = "tab";
}
