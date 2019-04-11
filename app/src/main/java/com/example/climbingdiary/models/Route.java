package com.example.climbingdiary.models;

import android.content.Context;
import android.database.Cursor;
import android.view.Menu;

import com.example.climbingdiary.MainActivity;
import com.example.climbingdiary.database.TaskRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Route {

    private int id;
    private String style;
    private String level;
    private String name;
    private String sector;
    private String area;
    private int rating;
    private String comment;
    private String date;
    private ArrayList<Route> routes;

    public Route(int _id, String _name, String _level, String _area, String _sector, String _style, int _rating, String _comment, String _date){
        id = _id;
        name = _name;
        level = _level;
        area = _area;
        sector = _sector;
        style = _style;
        rating = _rating;
        comment = _comment;
        date = _date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static Route getRoute(int _id){
        Route _route = null;
        TaskRepository taskRepository = new TaskRepository();
        taskRepository.open();
        //String Sort = (Menu) getA
        Cursor cursor = taskRepository.getRoute(_id);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String area = cursor.getString(2);
            String level = cursor.getString(3);
            String style = cursor.getString(4);
            int rating = cursor.getInt(5);
            String comment = cursor.getString(6);
            String date = cursor.getString(7);
            String sector = cursor.getString(8);
            _route = new Route(id,name,level,area,sector,style,rating,comment,date);
        }
        taskRepository.close();
        return _route;
    }

    public static ArrayList<Route> getRouteList() throws ParseException {
        ArrayList<Route> _routes = new ArrayList<>();
        TaskRepository taskRepository = new TaskRepository();
        taskRepository.open();
        //String Sort = (Menu) getA
        Cursor cursor = taskRepository.getAllRoutes(RouteSort.getSort(),null);
        if (cursor != null) {
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String area = cursor.getString(2);
                String level = cursor.getString(3);
                String style = cursor.getString(4);
                int rating = cursor.getInt(5);
                String comment = cursor.getString(6);
                String date = cursor.getString(7);
                String sector = cursor.getString(8);
                Route route = new Route(id,name,level,area,sector,style,rating,comment,date);
                _routes.add(route);
                cursor.moveToNext();
            }
        }
        taskRepository.close();
        return _routes;
    }
    public String toString(){
        return "Name:"+this.getName()+"||Area:"+this.getArea()+"||Sektor:"+this.getSector()+"||Level:"+this.getLevel()+"||Style:"+this.getStyle()+"||Rating:"+this.getRating()+"||Comment:"+this.getComment()+"||Date:"+this.getDate();
    }
}
