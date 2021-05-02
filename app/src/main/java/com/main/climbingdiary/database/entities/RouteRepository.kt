package com.main.climbingdiary.database.entities

import android.database.Cursor
import com.main.climbingdiary.common.AppPreferenceManager
import com.main.climbingdiary.database.TaskRepository
import com.main.climbingdiary.model.MenuValues
import org.chalup.microorm.MicroOrm
import java.util.*

class RouteRepository<T>(val klass: Class<T>) {

    private val uOrm = MicroOrm()

    fun getRoute(_id: Int): T {
        var cursor: Cursor? = null
        if (klass == Route::class.java) {
            cursor = TaskRepository.getRoute(_id)
        } else if (klass == Projekt::class) {
            cursor = TaskRepository.getProjekt(_id)
        }
        return uOrm.fromCursor(cursor, klass)
    }

    fun getRouteList(): ArrayList<T> {
        val routes = ArrayList<T>()
        var cursor: Cursor? = null
        if (klass == Route::class.java) {
            cursor = TaskRepository.getAllRoutes()
        } else if (klass == Projekt::class) {
            cursor = TaskRepository.getAllProjekts()
        }
        if (cursor != null) {
            while (!cursor.isAfterLast) {
                routes.add(uOrm.fromCursor(cursor, klass))
                cursor.moveToNext()
            }
        }
        return routes
    }

    fun getListByArea(areaId: Int): ArrayList<T>? {
        AppPreferenceManager.setFilterSetter(MenuValues.FILTER)
        AppPreferenceManager.setFilter(String.format("g.id = %s", areaId))
        val _routes = getRouteList()
        AppPreferenceManager.removeAllFilterPrefs()
        return _routes
    }

    fun deleteRoute(routeElement: RouteElement): Boolean {
        return if (routeElement is Projekt) {
            TaskRepository.deleteProjekt(routeElement.id)
        } else {
            TaskRepository.deleteRoute((routeElement as Route).id)
        }
    }

    fun insertRoute(route: Any?): Boolean {
        return if (route is Route) {
            TaskRepository.insertRoute(route as Route?)
        } else if (route is Projekt) {
            TaskRepository.insertProjekt(route as Projekt?)
        } else {
            false
        }
    }

    fun updateRoute(`object`: Any?): Boolean {
        return if (`object` is Route) {
            TaskRepository.updateRoute(`object` as Route?)
        } else if (`object` is Projekt) {
            TaskRepository.insertProjekt(`object` as Projekt?)
        } else {
            false
        }
    }

}