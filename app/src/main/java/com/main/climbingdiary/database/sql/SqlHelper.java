package com.main.climbingdiary.database.sql;

import com.main.climbingdiary.common.preferences.AppPreferenceManager;

import java.util.regex.Pattern;

public class SqlHelper {
    public static String getType() {
        return AppPreferenceManager.getSportType().typeToString();
    }
    public static boolean checkIfNumeric(String toCheck){
        final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (toCheck == null) {
            return false;
        }
        return pattern.matcher(toCheck).matches();
    }
}
