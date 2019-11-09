package com.main.climbingdiary.database.entities;

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
}
