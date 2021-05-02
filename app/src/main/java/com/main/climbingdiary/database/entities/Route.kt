package com.main.climbingdiary.database.entities

import org.chalup.microorm.annotations.Column

data class Route(
    @Column("id") val id: Int,
    @Column("stil") val style: String,
    @Column("level") val levelval: String,
    @Column("name") val name: String,
    @Column("sektor") val sector: String,
    @Column("gebiet") val area: String,
    @Column("rating") val rating: Int,
    @Column("kommentar") val comment: String,
    @Column("date") val date: String
):RouteElement
