package com.main.climbingdiary.database.entities;

import org.chalup.microorm.annotations.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Route implements RouteElement{
    @Column("id") private int id;
    @Column("stil") private String style;
    @Column("level") private String level;
    @Column("name") private String name;
    @Column("sektor") private String sector;
    @Column("gebiet") private String area;
    @Column("rating") private int rating;
    @Column("kommentar") private String comment;
    @Column("date") private String date;
}
