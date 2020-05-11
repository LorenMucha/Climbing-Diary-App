package com.main.climbingdiary.common;

public class StringUtil {
    public static String cleanDbString(String toClean){
        return toClean.replaceAll("'","`");
    }
}
