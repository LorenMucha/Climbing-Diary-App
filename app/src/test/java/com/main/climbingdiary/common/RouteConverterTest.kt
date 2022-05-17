package com.main.climbingdiary.common

import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.models.Styles
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

internal class RouteConverterTest {

    @Test
    fun routeToProjekt() {
        val testRoute = Route(0, Styles.getFLASH(),"9c","test","test","test",3,"test")
        val testProjekt = RouteConverter.routeToProjekt(testRoute)
        assertEquals(testProjekt.level,testRoute.level)
        assertEquals(testProjekt.area,testRoute.area)
        assertEquals(testProjekt.sector,testRoute.sector)
    }

    @Test
    fun cleanRoute() {
        val testRoute = RouteConverter.cleanRoute(Route(0, Styles.getFLASH(),
            "9c","`test","´test","test",3,"test")) as Route
        assertFalse(testRoute.name!!.contains("`"))
        assertFalse(testRoute.sector!!.contains("`"))
    }
}