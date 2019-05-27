package com.main.climbingdiary.models;

import android.database.Cursor;

import com.main.climbingdiary.database.TaskRepository;

import java.text.ParseException;
import java.util.ArrayList;

public class Projekt {
    private int id;
    private String level;
    private String name;
    private String sector;
    private String area;
    private int rating;
    private String comment;
    private ArrayList<Projekt> projekts;

    public Projekt(int _id, String _name, String _level, String _area, String _sector, int _rating, String _comment){
        id = _id;
        name = _name.replace("'","`");
        level = _level;
        area = _area.replace("'","`");
        sector = _sector;
        rating = _rating;
        comment = _comment.replace("'","`");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static Projekt getProjekt(int _id){
        Projekt _projekt = null;
        TaskRepository taskRepository = new TaskRepository();
        taskRepository.open();
        //String Sort = (Menu) getA
        Cursor cursor = taskRepository.getProjekt(_id);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String area = cursor.getString(2);
            String level = cursor.getString(3);
            int rating = cursor.getInt(4);
            String comment = cursor.getString(5);
            String sector = cursor.getString(6);
            _projekt = new Projekt(id,name,level,area,sector,rating,comment);
        }
        taskRepository.close();
        return _projekt;
    }

    public static ArrayList<Projekt> getProjektList() throws ParseException {
        ArrayList<Projekt> _projekte = new ArrayList<>();
        TaskRepository taskRepository = new TaskRepository();
        taskRepository.open();
        //String Sort = (Menu) getA
        Cursor cursor = taskRepository.getAllProjekts();
        if (cursor != null) {
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String area = cursor.getString(2);
                String level = cursor.getString(3);
                int rating = cursor.getInt(4);
                String comment = cursor.getString(5);
                String sector = cursor.getString(6);
                Projekt projekt = new Projekt(id,name,level,area,sector,rating,comment);
                _projekte.add(projekt);
                cursor.moveToNext();
            }
        }
        taskRepository.close();
        return _projekte;
    }

}
