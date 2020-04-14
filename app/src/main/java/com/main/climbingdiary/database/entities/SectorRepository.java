package com.main.climbingdiary.database.entities;

import android.content.Context;
import android.database.Cursor;

import com.main.climbingdiary.database.TaskRepository;

import java.util.ArrayList;

public interface SectorRepository {
    static ArrayList<String> getSectorList(Context context, String _area_name){
        ArrayList<String> _sector_list = new ArrayList<>();
        TaskRepository taskRepository = TaskRepository.getInstance();
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
