package com.example.climbingdiary.models;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.climbingdiary.database.TaskRepository;

import java.util.ArrayList;
import java.util.HashMap;

public class Area {
    private String country;
    private String name;
    private int id;
    //hashmap because null values are possible
    private HashMap<String, String> coordinates;

    public String getName() {
        return name;
    }

    public HashMap<String, String> getCoordinates() {
        return coordinates;
    }

    public String getCountry() {
        return country;
    }


    public Area(Builder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.coordinates = builder.coordinates;
        this.country = builder.country;
    }

    //builder pattern for optional paramters
    public static class Builder{
        private int id;
        private String name;
        private HashMap<String, String> coordinates;
        private String country;

        public Builder name(String _name){
            this.name = _name;
            return this;
        }
        public Builder id(int _id){
            this.id = _id;
            return this;
        }
        public Builder coordinates(HashMap<String, String> _coordinates){
            this.coordinates = _coordinates;
            return this;
        }
        public Builder country(String _country){
            this.country = _country;
            return this;
        }
        public Area build(){
            return new Area(this);
        }
    }
    public static ArrayList<Area> getRouteList(Context context){
        ArrayList<Area> _area_list = new ArrayList<>();
        TaskRepository taskRepository = new TaskRepository(context);
        taskRepository.open();
        Cursor cursor = taskRepository.getAllAreas();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String koordinaten = cursor.getString(2);
                String land = cursor.getString(3);

               Area area = new Builder().name(name).id(id).build();
               _area_list.add(area);
            }
        }
        return _area_list;
    }
    public static ArrayList<String> getRouteNameList(Context context){
        ArrayList<String> _area_list = new ArrayList<>();
        TaskRepository taskRepository = new TaskRepository(context);
        taskRepository.open();
        Cursor cursor = taskRepository.getAllAreas();
        if (cursor != null) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(1);
                _area_list.add(name);
                cursor.moveToNext();
            }
        }
        return _area_list;
    }
}
