package com.main.climbingdiary.database.entities

import org.chalup.microorm.annotations.Column

data class Projekt(
    @Column("id") val id: Int,
    @Column("level") val level: String,
@Column("name") val name: String,
@Column("sektor") val sector: String,
@Column("gebiet") val area: String,
@Column("rating") val rating:Int,
@Column("kommentar") val comment: String):RouteElement
