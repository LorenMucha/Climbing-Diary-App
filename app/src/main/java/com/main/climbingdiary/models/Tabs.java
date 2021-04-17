package com.main.climbingdiary.models;

public enum Tabs {
    STATISTIK, ROUTEN, BOULDER, PROJEKTE, MAP;

    public String typeToString() {
        String result = null;
        try {
            result = this.toString().toLowerCase();
        } catch (Exception ignored) {
        }
        return result;
    }

    public static Tabs stringToTabs(String type) {
        Tabs result = null;
        try {
            result = Tabs.valueOf(type.toUpperCase());
        }catch(Exception ignored){}
        return result;
    }
}
