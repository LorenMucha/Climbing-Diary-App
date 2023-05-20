package com.main.climbingdiary.database.entities

import com.main.climbingdiary.models.Styles
import org.chalup.microorm.annotations.Column

data class Route(
    @Column("id") var id: Int = 0,
    @Column("stil") var style: String = Styles.getRP(),
    @Column("level") override var level: String = "8a",
    @Column("name") override var name: String? = null,
    @Column("sektor") override var sector: String? = null,
    @Column("gebiet") override var area: String? = null,
    @Column("rating") var rating: Int? = null,
    @Column("kommentar") var comment: String? = null,
    @Column("date") var date: String? = null,
    @Column("tries") var tries: Int? = null
) : RouteElement