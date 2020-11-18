package com.main.climbingdiary.database.entities;

import android.database.Cursor;
import android.util.Log;

import com.main.climbingdiary.database.TaskRepository;

import java.util.ArrayList;

public interface ProjektRepository {
    static Projekt getProjekt(int _id) {
        Projekt _projekt = new Projekt();
        TaskRepository taskRepository = TaskRepository.getInstance();
        //String Sort = (Menu) getA
        Cursor cursor = taskRepository.getProjekt(_id);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            _projekt.setId(cursor.getInt(0));
            _projekt.setName(cursor.getString(1));
            _projekt.setArea(cursor.getString(2));
            _projekt.setLevel(cursor.getString(3));
            _projekt.setRating(cursor.getInt(4));
            _projekt.setComment(cursor.getString(5));
            _projekt.setSector(cursor.getString(6));

        }
        return _projekt;
    }

    static ArrayList<Projekt> getProjektList() {
        ArrayList<Projekt> _projekte = new ArrayList<>();
        TaskRepository taskRepository = TaskRepository.getInstance();
        //String Sort = (Menu) getA
        Cursor cursor = taskRepository.getAllProjekts();
        if (cursor != null) {
            while (!cursor.isAfterLast()) {
                Projekt projekt = new Projekt();
                projekt.setId(cursor.getInt(0));
                projekt.setName(cursor.getString(1));
                projekt.setArea(cursor.getString(2));
                projekt.setLevel(cursor.getString(3));
                projekt.setRating(cursor.getInt(4));
                projekt.setComment(cursor.getString(5));
                projekt.setSector(cursor.getString(6));
                _projekte.add(projekt);
                cursor.moveToNext();
            }
        }
        return _projekte;
    }

    static boolean deleteProjekt(int id) {
        Log.d("delete Projekt", Integer.toString(id));
        TaskRepository taskRepository = TaskRepository.getInstance();
        return taskRepository.deleteProjekt(id);
    }

    static boolean insertProjekt(Projekt projekt) {
        TaskRepository taskRepository = TaskRepository.getInstance();
        return taskRepository.insertProjekt(projekt);
    }
}
