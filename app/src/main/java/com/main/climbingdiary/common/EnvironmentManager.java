package com.main.climbingdiary.common;

import com.jakewharton.processphoenix.ProcessPhoenix;
import com.main.climbingdiary.activities.MainActivity;

public class EnvironmentManager {
    public static void restartApp(){
        ProcessPhoenix.triggerRebirth(MainActivity.getMainAppContext());
    }
}
