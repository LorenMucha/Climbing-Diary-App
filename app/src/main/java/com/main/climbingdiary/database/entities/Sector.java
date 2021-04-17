package com.main.climbingdiary.database.entities;

import org.chalup.microorm.annotations.Column;

import lombok.Data;

@Data
public class Sector {
    @Column("gebiet") private Integer area_name;
    @Column("name") private String name;
    @Column("id") private int id;
    @Column("lat") private double lat;
    @Column("lng") private double lng;
}
