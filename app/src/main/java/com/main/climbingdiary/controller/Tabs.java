package com.main.climbingdiary.controller;

import android.support.v4.app.Fragment;

import com.main.climbingdiary.fragments.MapFragment;
import com.main.climbingdiary.fragments.RouteDoneFragment;
import com.main.climbingdiary.fragments.RouteProjectFragment;
import com.main.climbingdiary.fragments.StatisticFragment;

public enum Tabs {
    STATISTIK,ROUTEN,PROJEKTE, MAP;
    public String getTitle(){
        String title;
        switch (this){
            case STATISTIK:
                title = StatisticFragment.TITLE;
                break;
            case PROJEKTE:
                title = RouteProjectFragment.TITLE;
                break;
            case MAP:
                title = MapFragment.TITLE;
                break;
            default:
                title= RouteDoneFragment.TITLE;
                break;
        }
        return title;
    }

    public Fragment getFragment(){
        Fragment fm;
        switch (this){
            case STATISTIK:
                fm = StatisticFragment.getInstance();
                break;
            case PROJEKTE:
                fm = RouteProjectFragment.getInstance();
                break;
            case MAP:
                fm = MapFragment.getInstance();
                break;
            default:
                fm = RouteDoneFragment.getInstance();
                break;
        }
        return fm;
    }
}
