package com.main.climbingdiary.database.entities

import org.chalup.microorm.annotations.Column

data class Area(
    @Column("land") var country: String,
    @Column("name") var name: String,
    @Column("id") var id: Int,
    @Column("lat") var lat: Double,
    @Column("lng") var lng: Double
)