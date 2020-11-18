package com.main.climbingdiary.models;

public class RouteSort {
    //default sort is by date
    private static String sort = "date";

    public static String getSort() {
        return sort;
    }

    public static void setSort(String _sort) {
        RouteSort.sort = _sort;
    }
}
