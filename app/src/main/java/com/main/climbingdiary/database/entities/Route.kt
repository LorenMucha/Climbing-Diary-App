package com.main.climbingdiary.database.entities

import org.chalup.microorm.annotations.Column

data class Route(
    @Column("id") var id: Int,
    @Column("stil") var style: String,
    @Column("level") var level: String,
    @Column("name") var name: String,
    @Column("sektor") var sector: String,
    @Column("gebiet") var area: String,
    @Column("rating") var rating: Int,
    @Column("kommentar") var comment: String,
    @Column("date") var date: String
):RouteElement
