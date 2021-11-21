package com.main.climbingdiary.database.entities

import org.chalup.microorm.annotations.Column

data class Area(
    @Column("land") var country: String = "",
    @Column("name") var name: String,
    @Column("id") var id: Int = 0,
    @Column("lat") var lat: Double = 0.0,
    @Column("lng") var lng: Double = 0.0
)