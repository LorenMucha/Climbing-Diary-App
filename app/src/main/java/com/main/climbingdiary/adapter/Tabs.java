package com.main.climbingdiary.adapter;

import android.support.v4.app.Fragment;

import com.main.climbingdiary.ProjectFragment;
import com.main.climbingdiary.RoutesFragment;
import com.main.climbingdiary.StatisticFragment;

public enum Tabs {
    STATISTIK,ROUTEN,PROJEKTE;
    public String getTitle(){
        String title;
        switch (this){
            case STATISTIK:
                title = "Statistik";
                break;
            case PROJEKTE:
                title =  "Projekte";
                break;
            default:
                title="Routen";
                break;
        }
        return title;
    }
    public Fragment getFragment(){
        Fragment fm;
        switch (this){
            case STATISTIK:
                fm = new StatisticFragment();
                break;
            case PROJEKTE:
                fm = new ProjectFragment();
                break;
            default:
                fm = new RoutesFragment();
                break;
        }
        return fm;
    }
}
