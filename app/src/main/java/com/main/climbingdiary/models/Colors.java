package com.main.climbingdiary.models;

import android.graphics.Color;

public abstract class Colors {
    public static int getMainColor(){
        return Color.parseColor("#0d47a1");
    }
    public static int getActiveColor(){
        return Color.parseColor("#007E33");
    }
    public static int getWarningColor(){
        return Color.parseColor("#FF8800");
    }
    public static int getDangerColor(){
        return Color.parseColor("#CC0000");
    }
    //must be a string1
    public static int getGradeColor(String _grade){
        if(_grade.contains("8")){
            return Color.parseColor("#212121");
        }else if(_grade.contains("7")){
            return Color.parseColor("#bf360c");
        }else{
            return Color.parseColor("#FF8800");
        }
    }
    public static int getStyleColor(String _style){
        switch (_style.toLowerCase()) {
            case "os":
                return Color.parseColor("#33b5e5");
            case "rp":
                return Color.parseColor("#0d47a1");
            default:
                return Color.parseColor("#00C851");
        }
    }
}
