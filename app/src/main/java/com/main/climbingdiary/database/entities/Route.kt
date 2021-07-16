package com.main.climbingdiary.database.entities

import com.main.climbingdiary.models.Styles
import org.chalup.microorm.annotations.Column

data class Route(
    @Column("id") var id: Int = 0,
    @Column("stil") var style: String = Styles.getRP(),
    @Column("level") var level: String = "8a",
    @Column("name") var name: String? = null,
    @Column("sektor") var sector: String? = null,
    @Column("gebiet") var area: String? = null,
    @Column("rating") var rating: Int? = null,
    @Column("kommentar") var comment: String? = null,
    @Column("date") var date: String? = null
) : RouteElement