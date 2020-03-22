package com.main.climbingdiary.models;

import java.util.Arrays;

public class Styles {
    public static String[] getStyle(Boolean UpperCase){
        if(UpperCase){
            return new String[]{"RP","FLASH","OS"};
        }else{
            return new String[]{"rp","flash","os"};
        }
    }

    public static int getStyleRatingFactor(String style){
        return (Arrays.asList(getStyle(false)).indexOf(style.toLowerCase())*5);
    }
}
