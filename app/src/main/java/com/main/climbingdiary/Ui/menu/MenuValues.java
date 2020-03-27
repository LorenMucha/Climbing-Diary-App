package com.main.climbingdiary.Ui.menu;

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
}
