package com.example.climbingdiary.models;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.climbingdiary.database.TaskRepository;

import java.util.ArrayList;
import java.util.HashMap;

public class Sector {
    private String area_name;
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
    public String getAreaName() {
        return area_name;
    }

    public Sector(Sector.Builder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.coordinates = builder.coordinates;
        this.area_name = builder.area_name;
    }

    //builder pattern for optional paramters
    public static class Builder{
        private int id;
        private String name;
        private HashMap<String, String> coordinates;
        private String area_name ;

        public Sector.Builder name(String _name){
            this.name = _name;
            return this;
        }
        public Sector.Builder id(int _id){
            this.id = _id;
            return this;
        }
        public Sector.Builder coordinates(HashMap<String, String> _coordinates){
            this.coordinates = _coordinates;
            return this;
        }
        public Sector.Builder area_name(String _area_name){
            this.area_name = _area_name;
            return this;
        }
        public Sector build(){
            return new Sector(this);
        }
    }

    public static ArrayList<String> getSectorList(Context context, String _area_name){
        ArrayList<String> _sector_list = new ArrayList<>();
        TaskRepository taskRepository = new TaskRepository(context);
        taskRepository.open();
        Cursor cursor = taskRepository.getSectorByAreaName(_area_name);
        if (cursor != null) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(0);
                _sector_list.add(name);
                cursor.moveToNext();
            }
        }
        return _sector_list;
    }
}
