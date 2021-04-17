package com.main.climbingdiary.models;

import android.util.Log;

public enum SportType {
    KLETTERN,BOULDERN;

    public String typeToString(){
        return this.toString().toLowerCase();
    }
    public static SportType stringToSportType(String type){
        return SportType.valueOf(type.toUpperCase());
    }
    public String getRouteName(){
        return this == KLETTERN ? "Routen" : "Boulder";
    }
}
