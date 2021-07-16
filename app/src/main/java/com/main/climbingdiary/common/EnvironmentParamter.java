package com.main.climbingdiary.common;

import com.main.climbingdiary.activities.MainActivity;

import java.time.LocalDate;

public class EnvironmentParamter {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "touren.db";
    public static final String DB_NAME_COPY = "touren_copy.db";
    public static final String dbExportName = String.format("%s_%s.db",
            EnvironmentParamter.DB_NAME.replace(".db", ""),
            LocalDate.now().toString().replace("/", "_"));
    public static final String PACKAGE_NAME = MainActivity.getMainAppContext().getPackageName();
    public static final String DB_PATH = "/data/" + EnvironmentParamter.PACKAGE_NAME + "/databases/" + DB_NAME;
    public static final String DB_PATH_COPY = "/data/" + EnvironmentParamter.PACKAGE_NAME + "/databases/" + DB_NAME_COPY;
}
