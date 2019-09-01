package com.main.climbingdiary.models.data;

import android.database.Cursor;

import com.main.climbingdiary.database.TaskRepository;

import java.text.ParseException;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
    private static TaskRepository taskRepository = new TaskRepository();

    public static Route getRoute(int _id){
        Route _route = null;
        taskRepository.open();
        //String Sort = (Menu) getA
        Cursor cursor = taskRepository.getRoute(_id);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            _route.setId(cursor.getInt(0));
            _route.setName(cursor.getString(1));
            _route.setArea(cursor.getString(2));
            _route.setLevel(cursor.getString(3));
            _route.setStyle(cursor.getString(4));
            _route.setRating(cursor.getInt(5));
            _route.setComment(cursor.getString(6));
            _route.setDate(cursor.getString(7));
            _route.setSector(cursor.getString(8));
        }
        taskRepository.close();
        return _route;
    }

    public static ArrayList<Route> getRouteList() throws ParseException {
        ArrayList<Route> _routes = new ArrayList<>();
        taskRepository.open();
        Cursor cursor = taskRepository.getAllRoutes();
        if (cursor != null) {
            while (!cursor.isAfterLast()) {
                Route _route = new Route();
                _route.setId(cursor.getInt(0));
                _route.setName(cursor.getString(1));
                _route.setArea(cursor.getString(2));
                _route.setLevel(cursor.getString(3));
                _route.setStyle(cursor.getString(4));
                _route.setRating(cursor.getInt(5));
                _route.setComment(cursor.getString(6));
                _route.setDate(cursor.getString(7));
                _route.setSector(cursor.getString(8));
                _routes.add(_route);
                cursor.moveToNext();
            }
        }
        taskRepository.close();
        return _routes;
    }

    public boolean deleteRoute(int id){
        taskRepository.open();
        boolean state = taskRepository.deleteRoute(id);
        taskRepository.close();
        return state;
    }
    public boolean insertRoute(){
        taskRepository.open();
        boolean state = taskRepository.inserRoute(this);
        taskRepository.close();
        return state;
    }
}
