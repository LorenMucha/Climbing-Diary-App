package com.main.climbingdiary.database.entities

import org.chalup.microorm.annotations.Column

data class Sector(
    @Column("gebiet") val area_name: Int,
    @Column("name") val name: String,
@Column("id") val id:Int,
@Column("lat") val lat:Int,
@Column("lng") val lng: Int
)
