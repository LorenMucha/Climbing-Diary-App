package com.main.climbingdiary.database.entities

import android.database.Cursor
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.database.TaskRepository
import com.main.climbingdiary.models.MenuValues
import org.chalup.microorm.MicroOrm
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

    fun deleteRoute(routeElement: RouteElement): Boolean {
        return if (routeElement is Projekt) {
            TaskRepository.deleteProjekt(routeElement.id)
        } else {
            TaskRepository.deleteRoute((routeElement as Route).id)
        }
    }

    fun insertRoute(route: RouteElement): Boolean {
        return if (route is Route) {
            TaskRepository.insertRoute(route)
        } else if (route is Projekt) {
            TaskRepository.insertProjekt(route)
        } else {
            false
        }
    }

    //Fixme: Method is to big
    fun updateRoute(toUpdate: RouteElement): Boolean {

        var area = Area(name = toUpdate.area!!)
        // check if area exists
        //Fixme update sector
        AreaRepository.getAreaByName(area.name)?.let {
            toUpdate.area = it.id.toString()
        } ?: run {
            AreaRepository.insertArea(area)
            area = AreaRepository.getAreaByName(area.name)!!
            toUpdate.area = area.id.toString()
        }
        //if numeric, the sector doesn´t exists
        SectorRepository.getSectorByAreaNameAndSectorName(
            sectorName = toUpdate.sector!!,
            areaName = area.name
        )?.let {
            toUpdate.sector = it.id.toString()
        } ?: run {
            val sector = Sector(name = toUpdate.sector!!, area = area.id)
            SectorRepository.insertSector(sector)
            toUpdate.sector = SectorRepository.getSectorByAreaNameAndSectorName(
                sector.name,
                area.name
            )!!.id.toString()
        }

        return when (toUpdate) {
            is Route -> {
                TaskRepository.updateRoute(toUpdate)
            }

            is Projekt -> {
                TaskRepository.insertProjekt(toUpdate)
            }

            else -> {
                false
            }
        }
    }
}