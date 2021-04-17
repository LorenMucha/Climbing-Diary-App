package com.main.climbingdiary.database.entities;

import android.database.Cursor;

import com.main.climbingdiary.database.TaskRepository;
import com.main.climbingdiary.models.SportType;

import org.chalup.microorm.MicroOrm;

import java.util.ArrayList;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AreaRepository {

    private final TaskRepository taskRepository;

    private static AreaRepository INSTANZ = null;
    private final MicroOrm uOrm = new MicroOrm();

    public static synchronized AreaRepository getInstance(){
        if(INSTANZ==null){
            INSTANZ = new AreaRepository(TaskRepository.getInstance());
        }
        return INSTANZ;
    }

    public Area getAreaByAreaNameAndSectorName(String sectorName, String areaName){
        Cursor cursor = taskRepository.getAreaIdByAreaNameAndSectorName(sectorName, areaName);
        return uOrm.fromCursor(cursor, Area.class);
    }

    public ArrayList<Area> getAreaList(){
        ArrayList<Area> _area_list = new ArrayList<>();
        Cursor cursor = taskRepository.getAllAreas();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                _area_list.add(uOrm.fromCursor(cursor, Area.class));
            }
        }
        return _area_list;
    }
    public ArrayList<String> getAreaNameList(){
        ArrayList<String> _area_list = new ArrayList<>();
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
