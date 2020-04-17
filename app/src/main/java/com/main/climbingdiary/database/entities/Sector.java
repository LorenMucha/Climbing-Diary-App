package com.main.climbingdiary.database.entities;

import lombok.Getter;

class Sector {
    @Getter
    private String area_name;
    @Getter
    private String name;
    @Getter
    private int id;
    @Getter
    private double lat;
    @Getter
    private double lng;

    private Sector(Sector.Builder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.lat = builder.lat;
        this.lng = builder.lng;
        this.area_name = builder.area_name;
    }

    //builder pattern for optional paramters
    public static class Builder{
        private int id;
        private String name;
        private double lat;
        private double lng;
        private String area_name ;

        public Sector.Builder name(String _name){
            this.name = _name;
            return this;
        }
        public Sector.Builder id(int _id){
            this.id = _id;
            return this;
        }
        public Sector.Builder lat(double _lat){
            this.lat = _lat;
            return this;
        }
        public Sector.Builder lng(double _lng){
            this.lng = _lng;
            return this;
        }
        public Sector build(){
            return new Sector(this);
        }
    }
}
