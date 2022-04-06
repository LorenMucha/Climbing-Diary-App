package com.main.climbingdiary.database.entities

import org.chalup.microorm.annotations.Column

data class Projekt(
    @Column("id") var id: Int = 0,
    @Column("level") override var level: String = "8a",
    @Column("name") override var name: String? = null,
    @Column("sektor") override var sector: String? = null,
    @Column("gebiet") override var area: String? = null,
    @Column("rating") var rating: Int? = null,
    @Column("kommentar") var comment: String? = null
) : RouteElement