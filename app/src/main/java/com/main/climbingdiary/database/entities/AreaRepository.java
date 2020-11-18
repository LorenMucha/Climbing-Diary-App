package com.main.climbingdiary.database.entities;

import android.database.Cursor;

import com.main.climbingdiary.database.TaskRepository;

import java.util.ArrayList;

public interface AreaRepository {
    static ArrayList<Area> getAreaList() {
        ArrayList<Area> _area_list = new ArrayList<>();
        TaskRepository taskRepository = TaskRepository.getInstance();
        Cursor cursor = taskRepository.getAllAreas();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                double lat = cursor.getDouble(2);
                double lng = cursor.getDouble(3);
                String country = cursor.getString(4);
                Area area = new Area.Builder()
                        .name(name)
                        .id(id)
                        .lat(lat)
                        .lng(lng)
                        .country(country)
                        .build();
                _area_list.add(area);
            }
        }
        return _area_list;
    }

    static ArrayList<String> getAreaNameList() {
        ArrayList<String> _area_list = new ArrayList<>();
        TaskRepository taskRepository = TaskRepository.getInstance();
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
