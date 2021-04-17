package com.main.climbingdiary.common;

import android.util.Log;

import com.main.climbingdiary.models.Levels;

import java.util.Arrays;

public class GradeConverter {

    public static String convertUiaaToFrench(String gradeUiaa) {
       try {
           int pos = Arrays.asList(Levels.getLevelsUiaa()).indexOf(gradeUiaa);
           return Levels.getLevelsFrench()[pos];
       }catch (Exception ex){
           Log.d("GradeConverter", ex.getLocalizedMessage());
           return null;
       }
    }

    public static String convertFrenchToUiaa(String french) {
        try {
            int pos = Arrays.asList(Levels.getLevelsFrench()).indexOf(french);
            return Levels.getLevelsUiaa()[pos];
        }catch (Exception ex){
            Log.d("GradeConverter", ex.getLocalizedMessage());
            return null;
        }
    }
}
