package com.main.climbingdiary.database.sql

import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.database.entities.AreaRepository
import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.SectorRepository
import com.main.climbingdiary.models.RouteSort
import com.main.climbingdiary.models.RouteType
import java.util.*

object SqlRouten {
    fun getProjekteTableName(): String {
        return "projekte_${AppPreferenceManager.getSportType().toString().toLowerCase(Locale.ROOT)}"
    }

    fun getRoutenTableName(): String {
        return "routen_${AppPreferenceManager.getSportType().toString().toLowerCase(Locale.ROOT)}"
    }

    fun getRouteList(routeType: RouteType): String {
        var filterSet: String = AppPreferenceManager.getFilter()!!
        filterSet = if (filterSet.isEmpty()) "" else String.format(" where %s", filterSet)
        val sort: RouteSort = AppPreferenceManager.getSort()!!
        var sql = ""
        if (routeType === RouteType.ROUTE) {
            when {
                sort === RouteSort.DATE -> {
                    sql = """SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, 
                            | r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor 
                            | FROM ${getRoutenTableName()} r
                            | join ${SqlAreaSektoren.getAreaTableName()} g on g.id=r.gebiet 
                            | join ${SqlAreaSektoren.getSektorenTableName()} k on k.id=r.sektor $filterSet 
                            | group by r.id Order By r.date DESC
                        """.trimMargin()
                }
                sort === RouteSort.AREA -> {
                    sql =
                        """SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, 
                            | strftime('%d.%m.%Y',r.date) as date, k.name as sektor 
                            | FROM ${getRoutenTableName()} r
                            | join ${SqlAreaSektoren.getAreaTableName()} g on g.id=r.gebiet
                            | join ${SqlAreaSektoren.getSektorenTableName()} k on k.id=r.sektor
                            | $filterSet
                            | group by r.id Order By g.name ASC
                        """.trimMargin()
                }
                else -> {
                    sql =
                        """SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, 
                            | strftime('%d.%m.%Y',r.date) as date, k.name as sektor 
                            | FROM ${getRoutenTableName()} r
                            | join ${SqlAreaSektoren.getAreaTableName()} g on g.id=r.gebiet
                            | join ${SqlAreaSektoren.getSektorenTableName()} k on k.id=r.sektor
                            | $filterSet
                            | group by r.id Order By r.level DESC
                        """.trimMargin()
                }
            }
        } else if (routeType === RouteType.PROJEKT) {
            if (sort === RouteSort.AREA) {
                sql = """SELECT r.id, r.name,g.name as gebiet,r.level,r.rating, r.kommentar, 
                    | k.name as sektor 
                    | FROM ${getProjekteTableName()} r
                    | join ${SqlAreaSektoren.getAreaTableName()} g on g.id=r.gebiet 
                    | join ${SqlAreaSektoren.getSektorenTableName()} k on k.id=r.sektor
                    | $filterSet
                    | group by r.id Order By g.name ASC
                """.trimMargin()
            } else {
                sql = """SELECT r.id, r.name,g.name as gebiet,r.level, r.rating, 
                    | r.kommentar, k.name as sektor 
                    | FROM ${getProjekteTableName()} r
                    | join ${SqlAreaSektoren.getAreaTableName()} g on g.id=r.gebiet
                    | join ${SqlAreaSektoren.getSektorenTableName()} k on k.id=r.sektor
                    | $filterSet
                    | group by r.id Order By r.level DESC
                """.trimMargin()
            }
        }
        return sql
    }

    fun getRoute(id: Int): String {
        return """SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, 
            | strftime('%d.%m.%Y',r.date) as date, k.name as sektor 
            | FROM ${getRoutenTableName()} r, 
            | ${SqlAreaSektoren.getAreaTableName()} g, 
            | ${SqlAreaSektoren.getSektorenTableName()} k
            | where g.id=r.gebiet and k.id=r.sektor AND r.id=$id""".trimMargin()
    }

    fun getProjekt(id: Int): String {
        return """SELECT r.id, r.name,g.name as gebiet,r.level,r.rating, r.kommentar,
             | k.name as sektor 
             | FROM ${getProjekteTableName()} r,
             | ${SqlAreaSektoren.getAreaTableName()} g, 
             | ${SqlAreaSektoren.getSektorenTableName()} k
             | where g.id=r.gebiet and k.id=r.sektor AND r.id=$id""".trimMargin()
    }

    fun getTopTenRoutes(year: Int): String {
        return """Select r.level, r.stil, r.name, r.gebiet 
            | FROM ${getRoutenTableName()} r
            | where CAST(strftime('%Y',r.date) as int)==$year
            | Order By r.level desc, r.stil asc Limit 10""".trimMargin()
    }

    fun getYears(filterSet: Boolean): String {
        val filter: String = if (filterSet && AppPreferenceManager.getFilter()!!.isNotEmpty())
            AppPreferenceManager.getFilter()!! else ""
        return """select DISTINCT(strftime('%Y',r.date)) as year from ${getRoutenTableName()} r 
            | $filter order by r.date DESC""".trimMargin()
    }

    fun getInsertRouteTasks(route: Route): Array<String> {
        val insertArea =
            "INSERT OR IGNORE INTO ${SqlAreaSektoren.getAreaTableName()} (name) VALUES ('${route.area}')"
        val insertSektor =
            """INSERT OR IGNORE INTO ${SqlAreaSektoren.getSektorenTableName()} (name,gebiet)
                    | SELECT '${route.sector}',
                    | id FROM ${SqlAreaSektoren.getAreaTableName()}
                    | WHERE name='${route.area}'""".trimMargin()
        val insertRoute =
            """INSERT OR IGNORE INTO ${getRoutenTableName()}
                | (date,name,level,stil,rating,kommentar,gebiet,sektor) 
                | SELECT '${route.date}','${route.name}','${route.level}',
                | '${route.style}','${route.rating}','${route.comment}',a.id,s.id
                | FROM ${SqlAreaSektoren.getAreaTableName()} a, ${SqlAreaSektoren.getSektorenTableName()} s
                | WHERE a.name = '${route.area}'
                | AND s.name='${route.sector}'""".trimMargin()
        return arrayOf(
            insertArea, insertSektor, insertRoute
        )
    }

    fun getInsertProjektTasks(projekt: Projekt): Array<String> {
        val insertArea =
            "INSERT OR IGNORE INTO ${SqlAreaSektoren.getAreaTableName()} (name) VALUES ('${projekt.area}')"
        val insertSektor =
            """INSERT OR IGNORE INTO ${SqlAreaSektoren.getSektorenTableName()} (name,gebiet)
               | SELECT '${projekt.sector}',id FROM ${
                SqlAreaSektoren.getAreaTableName().toString()
            } 
               | WHERE name='${projekt.area}'""".trimMargin()
        val insertProjekt =
            """INSERT OR IGNORE INTO ${getProjekteTableName()} (name,level,rating,kommentar,gebiet,sektor)
               | SELECT '${projekt.name}','${projekt.level}','${projekt.rating}','${projekt.comment}',a.id,s.id
               | FROM "${SqlAreaSektoren.getAreaTableName()} a, ${SqlAreaSektoren.getSektorenTableName()} s 
               | WHERE a.name = '${projekt.area}'
               | AND s.name='${projekt.sector}'""".trimMargin()
        return arrayOf(insertArea, insertSektor, insertProjekt)
    }

    fun updateRoute(route: Route): String {
        val areaName = route.area!!
        val sectorName = route.sector!!
        if (checkIfNumeric(areaName) && checkIfNumeric(sectorName)) {
            route.area =
                AreaRepository.getAreaByAreaNameAndSectorName(sectorName, areaName).id.toString()
            route.sector = SectorRepository.getSectorByAreaNameAndSectorName(
                sectorName,
                areaName
            ).id.toString()
        }
        return """ UPDATE ${getRoutenTableName()} SET
                    | date = '${route.date}',
                    | name = '${route.name}',
                    | level = '${route.level}',
                    | gebiet = '${route.area}',
                    | sektor = '${route.sector}',
                    | stil = '${route.style}',
                    | rating = '${route.rating}',
                    | kommentar = '${route.comment}'
                    | where id =${route.id}
                    """.trimMargin()
    }

    fun deleteRoute(id: Int): String {
        return "DELETE FROM ${getRoutenTableName()} WHERE id=$id"
    }

    fun deleteProjekt(id: Int): String {
        return "DELETE FROM ${getProjekteTableName()} WHERE id=$id"
    }

    fun checkIfNumeric(toCheck: String): Boolean {
        val regex = "-?\\d+(\\.\\d+)?".toRegex()
        return !regex.containsMatchIn(toCheck)
    }
}