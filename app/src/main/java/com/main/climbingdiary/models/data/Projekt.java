package com.main.climbingdiary.models.data;

import android.database.Cursor;
import android.util.Log;

import com.main.climbingdiary.database.TaskRepository;

import java.text.ParseException;
import java.util.ArrayList;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Projekt {
    private int id;
    private String level;
    private String name;
    private String sector;
    private String area;
    private int rating;
    private String comment;
    private ArrayList<Projekt> projekts;
    private static TaskRepository taskRepository = new TaskRepository();
    public static Projekt _projekt;

    public static Projekt getProjekt(){
        return _projekt;
    }

    public static Projekt getProjekt(int _id){
        _projekt = new Projekt();
        taskRepository.open();
        //String Sort = (Menu) getA
        Cursor cursor = taskRepository.getProjekt(_id);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            _projekt.setId(cursor.getInt(0));
            _projekt.setName(cursor.getString(1));
            _projekt.setArea(cursor.getString(2));
            _projekt.setLevel(cursor.getString(3));
            _projekt.setRating(cursor.getInt(4));
            _projekt.setComment(cursor.getString(5));
            _projekt.setSector(cursor.getString(6));

        }
        taskRepository.close();
        System.out.println("-------------------------"+_projekt.toString());
        return _projekt;
    }

    public static ArrayList<Projekt> getProjektList() throws ParseException {
        ArrayList<Projekt> _projekte = new ArrayList<>();
        taskRepository.open();
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
        taskRepository.close();
        return _projekte;
    }

    public boolean deleteProjekt(int id){
        Log.d("delete Projekt",Integer.toString(id));
        taskRepository.open();
        boolean state = taskRepository.deleteProjekt(id);
        taskRepository.close();
        return state;
    }
    public boolean insertProjekt(){
        taskRepository.open();
        boolean state = taskRepository.inserProjekt(this);
        taskRepository.close();
        return state;
    }
}
