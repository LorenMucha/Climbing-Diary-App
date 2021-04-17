package com.main.climbingdiary.database.entities;

import android.database.Cursor;

import com.main.climbingdiary.database.TaskRepository;

import org.chalup.microorm.MicroOrm;

import java.util.ArrayList;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SectorRepository {

    private static SectorRepository INSTANZ = null;
    private final TaskRepository taskRepository;
    private final MicroOrm uOrm = new MicroOrm();

    public static synchronized SectorRepository getInstance() {
        if (INSTANZ == null) {
            INSTANZ = new SectorRepository(TaskRepository.getInstance());
        }
        return INSTANZ;
    }

    public Sector getSectorByAreaNameAndSectorName(String sectorName, String areaName){
        Cursor cursor = taskRepository.getSectorIdByAreaNameAndSectorName(sectorName, areaName);
        return uOrm.fromCursor(cursor, Sector.class);
    }

    public ArrayList<String> getSectorList(String _area_name) {
        ArrayList<String> _sector_list = new ArrayList<>();
        Cursor cursor = taskRepository.getSectorListByAreaName(_area_name);
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
