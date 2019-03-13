package com.example.climbingdiary.models;

public class Styles {
    public static String[] getStyle(Boolean UpperCase){
        if(UpperCase){
            return new String[]{"OS","RP","FLASH"};
        }else{
            return new String[]{"os","rp","flash"};
        }
    }
}
