package com.main.climbingdiary.database.entities;

import org.chalup.microorm.annotations.Column;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Projekt implements RouteElement{
    @Column("id") private int id;
    @Column("level") private String level;
    @Column("name") private String name;
    @Column("sektor") private String sector;
    @Column("gebiet") private String area;
    @Column("rating") private int rating;
    @Column("kommentar") private String comment;
}
