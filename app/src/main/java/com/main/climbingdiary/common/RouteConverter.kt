package com.main.climbingdiary.common

import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.Route
import java.text.SimpleDateFormat
import java.util.*

object RouteConverter {

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

    fun projektToRoute(projekt: Projekt):Route{
        val sdf =
            SimpleDateFormat("YYYY-MM-dd", Locale.GERMAN)
        val route = Route()
        route.id = projekt.id
        route.name = projekt.name
        route.area = projekt.area
        route.level = projekt.level
        route.sector = projekt.sector
        route.date = sdf.format(Date())
        route.rating = projekt.rating!!
        route.comment = projekt.comment
        route.style = "rp"
        return route
    }

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
        val badCharacter = """[`´]""".toRegex()
        return toClean!!.replace(badCharacter, "'")
    }
}