package com.main.climbingdiary.common

import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.Route

object RouteConverter {

    fun routeToProjekt(route: Route): Projekt {
        return Projekt(
            route.id,
            route.level,
            route.name,
            route.sector,
            route.area,
            route.rating,
            route.comment
        )
    }

    fun cleanRoute(route: Any): Any {
        return if (route is Projekt) {
            route.name =cleanString(route.name)
            route.area = cleanString(route.area)
            route.sector = cleanString(route.sector)
            route.comment =cleanString(route.comment)
            route
        } else {
            val routeSet = route as Route
            routeSet.name = cleanString(routeSet.name)
            routeSet.area = cleanString(routeSet.area)
            routeSet.sector = cleanString(routeSet.sector)
            routeSet.comment = cleanString(routeSet.comment)
            routeSet
        }
    }

    private fun cleanString(toClean: String?): String {
        return toClean!!.replace("`", "'")
    }
}