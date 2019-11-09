package com.main.climbingdiary.database.entities;

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
}
