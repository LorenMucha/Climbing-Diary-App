package com.main.climbingdiary.common;

import android.util.Log;

import com.main.climbingdiary.models.Levels;

import java.util.Arrays;

public class GradeConverter {

    public static String convertUiaaToFrench(String gradeUiaa) {
        try {
            int pos = Arrays.asList(Levels.INSTANCE.getLevelsUiaa()).indexOf(gradeUiaa);
            return Levels.INSTANCE.getLevelsFrench()[pos];
        } catch (Exception ex) {
            Log.d("GradeConverter", ex.getLocalizedMessage());
            return null;
        }
    }

    public static String convertFrenchToUiaa(String french) {
        try {
            int pos = Arrays.asList(Levels.INSTANCE.getLevelsFrench()).indexOf(french);
            return Levels.INSTANCE.getLevelsUiaa()[pos];
        } catch (Exception ex) {
            Log.d("GradeConverter", ex.getLocalizedMessage());
            return null;
        }
    }
}
