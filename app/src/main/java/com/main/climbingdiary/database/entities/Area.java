package com.main.climbingdiary.database.entities;

import lombok.Getter;
import lombok.ToString;

@ToString
public class Area {
    @Getter
    private String country;
    @Getter
    private String name;
    @Getter
    private int id;
    @Getter
    private double lat;
    @Getter
    private double lng;

    private Area(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.lat = builder.lat;
        this.lng = builder.lng;
        this.country = builder.country;
    }

    public static class Builder {
        private int id;
        private String name;
        private double lat;
        private double lng;
        private String country;

        public Builder name(String _name) {
            this.name = _name;
            return this;
        }

        public Builder id(int _id) {
            this.id = _id;
            return this;
        }

        public Builder lat(double _lat) {
            this.lat = _lat;
            return this;
        }

        public Builder lng(double _lng) {
            this.lng = _lng;
            return this;
        }

        public Builder country(String _country) {
            this.country = _country;
            return this;
        }

        public Area build() {
            return new Area(this);
        }
    }
}
