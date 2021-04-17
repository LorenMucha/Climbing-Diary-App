package com.main.climbingdiary.database.entities;

import org.chalup.microorm.annotations.Column;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Area {
    @Column("land") private String country;
    @Column("name") private String name;
    @Column("id") private int id;
    @Column("lat") private double lat;
    @Column("lng") private double lng;
}
