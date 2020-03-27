package com.main.climbingdiary.common.preferences;

import com.main.climbingdiary.R;
import com.main.climbingdiary.activities.MainActivity;

public class PreferenceKeys {
    public static String DB_OUTPUT_PATH = MainActivity.getMainActivity().getString(R.string.db_output_path);
    public static String SAFTY_COPY = MainActivity.getMainActivity().getString(R.string.safty_copy);
    public static String RESTORE_COPY = MainActivity.getMainActivity().getString(R.string.safty_restore);
}
