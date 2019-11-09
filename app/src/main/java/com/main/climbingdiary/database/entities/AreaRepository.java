package com.main.climbingdiary.database.entities;

import android.database.Cursor;

import com.main.climbingdiary.database.TaskRepository;

import java.util.ArrayList;

public interface AreaRepository {
    static ArrayList<Area> getAreaList(){
        ArrayList<Area> _area_list = new ArrayList<>();
        TaskRepository taskRepository = new TaskRepository();
        taskRepository.open();
        Cursor cursor = taskRepository.getAllAreas();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String koordinaten = cursor.getString(2);
                String land = cursor.getString(3);

                Area area = new Area.Builder().name(name).id(id).build();
                _area_list.add(area);
            }
        }
        taskRepository.close();
        return _area_list;
    }
    static ArrayList<String> getAreaNameList(){
        ArrayList<String> _area_list = new ArrayList<>();
        TaskRepository taskRepository = new TaskRepository();
        taskRepository.open();
        Cursor cursor = taskRepository.getAllAreas();
        if (cursor != null) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(1);
                _area_list.add(name);
                cursor.moveToNext();
            }
        }
        taskRepository.close();
        return _area_list;
    }
}
