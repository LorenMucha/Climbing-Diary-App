package com.main.climbingdiary.database.entities;

import android.database.Cursor;

import com.main.climbingdiary.database.TaskRepository;

import java.util.ArrayList;

public interface RouteRepository {
    static Route getRoute(int _id){
        Route _route = new Route();
        TaskRepository taskRepository = TaskRepository.getInstance();
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
        return _route;
    }
    static ArrayList<Route> getRouteList() {
        ArrayList<Route> _routes = new ArrayList<>();
        TaskRepository taskRepository = TaskRepository.getInstance();
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
        return _routes;
    }

    static boolean deleteRoute(int id){
        TaskRepository taskRepository = TaskRepository.getInstance();
        return taskRepository.deleteRoute(id);
    }
    static boolean insertRoute(Route route){
        TaskRepository taskRepository = TaskRepository.getInstance();
        return taskRepository.inserRoute(route);
    }
}
