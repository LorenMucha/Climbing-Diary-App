package com.main.climbingdiary.common

import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.Route

object RouteConverter {
    @JvmStatic
    fun routeToProjekt(route: Route): Projekt {
        val projekt = Projekt()
        projekt.id = route.id
        projekt.level = route.level
        projekt.name = route.name
        projekt.sector = route.sector
        projekt.area = route.area
        projekt.rating = route.rating
        projekt.comment = route.comment
        return projekt
    }
    @JvmStatic
    fun cleanRoute(route: Any): Any {
        return if (route is Projekt) {
            route.name = cleanString(route.name)
            route.area = cleanString(route.area)
            route.sector = cleanString(route.sector)
            route.comment = cleanString(route.comment)
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