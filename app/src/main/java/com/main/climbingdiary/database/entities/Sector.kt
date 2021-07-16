package com.main.climbingdiary.database.entities

import org.chalup.microorm.annotations.Column

data class Sector(
    @Column("gebiet") var area_name: Int,
    @Column("name") var name: String,
    @Column("id") var id: Int,
    @Column("lat") var lat: Int,
    @Column("lng") var lng: Int
)
