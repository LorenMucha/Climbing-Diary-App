package com.main.climbingdiary.database.entities

import com.main.climbingdiary.models.Styles
import org.chalup.microorm.annotations.Column

data class Projekt(
    @Column("id") var id: Int = 0,
    @Column("level") var level: String = Styles.getRP(),
    @Column("name") var name: String? = null,
    @Column("sektor") var sector: String? = null,
    @Column("gebiet") var area: String? = null,
    @Column("rating") var rating: Int? = null,
    @Column("kommentar") var comment: String? = null
) : RouteElement