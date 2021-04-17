package com.main.climbingdiary.common;

import com.main.climbingdiary.database.entities.Projekt;
import com.main.climbingdiary.database.entities.Route;

import lombok.var;

public class RouteConverter {
    public static Projekt routeToProjekt(Route route){
        Projekt projekt = new Projekt();
        projekt.setId(route.getId());
        projekt.setLevel(route.getLevel());
        projekt.setName(route.getName());
        projekt.setSector(route.getSector());
        projekt.setArea(route.getArea());
        projekt.setRating(route.getRating());
        projekt.setComment(route.getComment());
        return projekt;
    }

    public static Object cleanRoute(Object route){
        if(route instanceof Projekt) {
            var routeSet = (Projekt) route;
            routeSet.setName(cleanString(routeSet.getName()));
            routeSet.setArea(cleanString(routeSet.getArea()));
            routeSet.setSector(cleanString(routeSet.getSector()));
            routeSet.setComment(cleanString(routeSet.getComment()));
            return routeSet;
        }else{
            var routeSet = (Route) route;
            routeSet.setName(cleanString(routeSet.getName()));
            routeSet.setArea(cleanString(routeSet.getArea()));
            routeSet.setSector(cleanString(routeSet.getSector()));
            routeSet.setComment(cleanString(routeSet.getComment()));
            return routeSet;
        }
    }

    private static String cleanString(String toClean){
        return toClean.replace("`","'");
    }
}
