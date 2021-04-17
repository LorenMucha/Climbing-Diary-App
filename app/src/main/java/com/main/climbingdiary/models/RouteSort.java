package com.main.climbingdiary.models;

import android.util.Log;

public enum RouteSort {
    //default sort is by date
    LEVEL,AREA,DATE;
    public String typeToString(){
        return this.toString().toLowerCase();
    }

    public static RouteSort stringToSportType(String type){
        RouteSort value = null;
        try {
            value = RouteSort.valueOf(type.toUpperCase());
        }catch(Exception ex){
            Log.d("Exception in MenuValues stringToSportType() ",ex.getLocalizedMessage());
        }
        return value;
    }
}
