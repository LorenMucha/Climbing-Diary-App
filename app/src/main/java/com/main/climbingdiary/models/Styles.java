package com.main.climbingdiary.models;

import java.util.Arrays;

public class Styles {
    public static String[] getStyle(Boolean UpperCase){
        if(UpperCase){
            return new String[]{"OS","RP","FLASH"};
        }else{
            return new String[]{"os","rp","flash"};
        }
    }

    public static int getStyleRatingFactor(String style){
        return (Arrays.asList(getStyle(false)).indexOf(style.toLowerCase())*5);
    }
}
