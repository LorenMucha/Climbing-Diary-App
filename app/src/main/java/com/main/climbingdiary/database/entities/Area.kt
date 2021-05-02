package com.main.climbingdiary.database.entities

import org.chalup.microorm.annotations.Column

data class Area(
    @Column("land") val country: String,
    @Column("name") val name: String,
    @Column("id") val id: Int,
    @Column("lat") val lat: Double,
    @Column("lng") val lng: Double
)
