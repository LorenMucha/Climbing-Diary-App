package com.main.climbingdiary.common;

import com.main.climbingdiary.MainActivity;

import java.time.LocalDate;

public class EnvironmentParamter {
    public static String DB_NAME = "touren.db";
    public static String dbExportName = String.format("%s_%s.db",
            EnvironmentParamter.DB_NAME.replace(".db",""),
            LocalDate.now().toString().replace("/","_"));
    public static String PACKAGE_NAME = MainActivity.getMainAppContext().getPackageName();
}
