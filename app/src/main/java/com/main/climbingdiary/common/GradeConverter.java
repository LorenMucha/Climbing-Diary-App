package com.main.climbingdiary.common;

import com.main.climbingdiary.models.Levels;

import java.util.Arrays;

public class GradeConverter {

    public static String convertUiaaToFrech(String gradeUiaa) {
       int pos = Arrays.asList(Levels.getLevelsUiaa()).indexOf(gradeUiaa);
       return Levels.getLevelsFrench()[pos];
    }
}
