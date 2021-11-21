package com.main.climbingdiary.database.entities

import org.chalup.microorm.annotations.Column

data class Sector(
    @Column("gebiet") var area:Int,
    @Column("name") var name: String,
    @Column("id") var id: Int =0,
    @Column("lat") var lat: Double = 0.0,
    @Column("lng") var lng: Double = 0.0
)
