package com.main.climbingdiary.database.sql;

import android.util.Log;

import com.main.climbingdiary.common.preferences.AppPreferenceManager;


public class SqlAreaSektoren {

    public static String getAreaTableName() {
        return "gebiete_" +  AppPreferenceManager.getSportType().typeToString();
    }

    public static String getSektorenTableName() {
        return "sektoren_" +  AppPreferenceManager.getSportType().typeToString();
    }

    public static String getAllAreas(){
        return "SELECT * FROM "+getAreaTableName()+" Order By name";
    }

    public static String getSectorIdBySectorNameAndAreaName(String sectorName, String areaName){
        final String query = String.format("SELECT s.* FROM %s s, %s a where s.name='%s' AND a.name='%s'",
                getSektorenTableName(), getAreaTableName(), sectorName, areaName);
        Log.d("get Query:",query);
        return query;
    }

    public static String getAreaIdBySectorNameAndAreaName(String sectorName, String areaName){
        return String.format("SELECT * FROM %s s, %s a where s.name='%s' AND a.name='%s'",
                getSektorenTableName(), getAreaTableName(), sectorName, areaName);
    }

    public static String getSectorByAreaName(String name){
        return "SELECT s.name, s.gebiet,s.id FROM "
                +getSektorenTableName()+
                " s, "
                +getAreaTableName()+
                " a where a.name Like '"
                + name +
                "%' and s.gebiet=a.id GROUP BY s.id";
    }
}
