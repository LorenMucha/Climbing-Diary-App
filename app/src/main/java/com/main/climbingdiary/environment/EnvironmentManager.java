package com.main.climbingdiary.environment;

import com.main.climbingdiary.MainActivity;

public class EnvironmentManager {
    public static String getPackageName(){
        return MainActivity.getMainAppContext().getPackageName();
    }
    public static String getDbPath(){
        return String.format("//data/data/%s/databases/%s",getPackageName(),"touren.db");
    }
}
