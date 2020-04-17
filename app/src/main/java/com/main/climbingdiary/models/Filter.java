package com.main.climbingdiary.models;

import com.main.climbingdiary.controller.menu.MenuValues;

public class Filter {
    private static String filter = null;
    private static MenuValues setter = null;
    public static void setFilter(String _filter, MenuValues _setter) {
        filter = _filter;
        setter = _setter;
    }
    public static MenuValues getSetter(){
        return setter;
    }
    public static void removeFilter(){
        filter=null;
        setter=null;
    }
    public static String getFilter(){return filter;}
    public static String getFilter(boolean isString){
        if(isString && filter==null){ return "";}
        else{return filter;}
    }
}
