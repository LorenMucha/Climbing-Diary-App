package com.main.climbingdiary.database.entities

import android.database.Cursor
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.database.DatabaseHelper.Companion.checkIfNumeric
import com.main.climbingdiary.database.TaskRepository
import com.main.climbingdiary.models.MenuValues
import org.chalup.microorm.MicroOrm
import java.util.*
import kotlin.reflect.KClass

class RouteRepository<T : RouteElement>(private val klass: KClass<T>) {

    private val uOrm = MicroOrm()

    fun getRoute(_id: Int): T {
        var cursor: Cursor? = null
        if (klass == Route::class) {
            cursor = TaskRepository.getRoute(_id)
        } else if (klass == Projekt::class) {
            cursor = TaskRepository.getProjekt(_id)
        }
        return uOrm.fromCursor(cursor, klass.java)
    }

    fun getRouteList(): ArrayList<T> {
        val routes = ArrayList<T>()
        var cursor: Cursor? = null
        if (klass == Route::class) {
            cursor = TaskRepository.getAllRoutes()
        } else if (klass == Projekt::class) {
            cursor = TaskRepository.getAllProjekts()
        }
        if (cursor != null) {
            while (!cursor.isAfterLast) {
                routes.add(uOrm.fromCursor(cursor, klass.java))
                cursor.moveToNext()
            }
        }
        return routes
    }

    fun getListByArea(areaId: Int): ArrayList<T> {
        AppPreferenceManager.setFilterSetter(MenuValues.FILTER)
        AppPreferenceManager.setFilter(String.format("g.id = %s", areaId))
        val routes = getRouteList()
        AppPreferenceManager.removeAllFilterPrefs()
        return routes
    }

    fun deleteRoute(routeElement: RouteElement): Boolean {
        return if (routeElement is Projekt) {
            TaskRepository.deleteProjekt(routeElement.id)
        } else {
            TaskRepository.deleteRoute((routeElement as Route).id)
        }
    }

    fun insertRoute(route: RouteElement): Boolean {
        route.let {
            var area = Area(name = it.area!!)
            // if not numeric, the area doesn´t exists
            if (!checkIfNumeric(it.area!!)) {
                AreaRepository.insertArea(area)
                area = AreaRepository.getAreaByName(area.name)
                it.area = area.id.toString()
            }
            //if numeric, the sector doesn´t exists
            if (!checkIfNumeric(it.sector!!)) {
                val sector = Sector(name = it.sector!!, area = area.id)
                SectorRepository.insertSector(sector)
                it.sector = SectorRepository.getSectorByAreaNameAndSectorName(
                    sector.name,
                    area.name
                ).id.toString()
            }
        }

        return if (route is Route) {
            TaskRepository.insertRoute(route)
        } else if (route is Projekt) {
            TaskRepository.insertProjekt(route)
        } else {
            false
        }
    }

    fun updateRoute(toUpdate: RouteElement?): Boolean {
        return if (toUpdate is Route) {
            TaskRepository.updateRoute(toUpdate)
        } else if (toUpdate is Projekt) {
            TaskRepository.insertProjekt(toUpdate)
        } else {
            false
        }
    }


}