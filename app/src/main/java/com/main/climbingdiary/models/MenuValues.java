package com.main.climbingdiary.models;

import android.util.Log;

public enum MenuValues {
    SEARCH,FILTER,SORT,SORT_DATE,SETTINGS;

    public String toString(){
        String value = "";
        switch (this){
            case SEARCH:
                value = "search";
                break;
            case FILTER:
                value="filter";
                break;
            case SORT:
                value = "sort";
                break;
            case SORT_DATE:
                value = "date";
                break;
            case SETTINGS:
                value = "settings";
                break;
        }
        return value;
    }

    public String typeToString(){
        return this.toString().toLowerCase();
    }

    public static MenuValues stringToSportType(String type){
           return MenuValues.valueOf(type.toUpperCase());
    }
}
