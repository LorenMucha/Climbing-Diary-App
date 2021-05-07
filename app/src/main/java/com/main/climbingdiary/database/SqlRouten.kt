package com.main.climbingdiary.database

import com.main.climbingdiary.common.AppPreferenceManager
import com.main.climbingdiary.database.entities.AreaRepository
import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.SectorRepository
import com.main.climbingdiary.database.sql.SqlAreaSektoren.getAreaTableName
import com.main.climbingdiary.database.sql.SqlAreaSektoren.getSektorenTableName
import com.main.climbingdiary.database.sql.SqlRouten
import com.main.climbingdiary.database.sql.SqlRouten.getProjekteTableName
import com.main.climbingdiary.database.sql.SqlRouten.getRoutenTableName
import com.main.climbingdiary.model.RouteSort
import com.main.climbingdiary.model.RouteType
import java.util.*

/*
Fixme: String concat neu schreiben im Kotlin Style
 */
object SqlRouten {

    val type = AppPreferenceManager.getSportType().toString().toLowerCase(Locale.ROOT)
    fun getRouteList(routeType: RouteType): String {
        val builder = StringBuilder()
        var filterSet = AppPreferenceManager.getFilter()
        filterSet = if (filterSet.isEmpty()) "" else String.format(" where %s", filterSet)
        val sort: RouteSort = AppPreferenceManager.getSort()
        if (routeType === RouteType.ROUTE) {
            if (sort === RouteSort.DATE) {
                builder.append("SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM ")
                    .append(getRoutenTableName())
                    .append(" r")
                    .append(
                        java.lang.String.format(
                            " join %s g on g.id=r.gebiet join %s k on k.id=r.sektor %s group by r.id Order By r.date DESC",
                            getAreaTableName(),
                            getSektorenTableName(),
                            filterSet
                        )
                    )
            } else if (sort === RouteSort.AREA) {
                builder.append("SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM ")
                    .append(getRoutenTableName())
                    .append(" r")
                    .append(" join ")
                    .append(getAreaTableName())
                    .append(" g on g.id=r.gebiet join ")
                    .append(getSektorenTableName())
                    .append(" k on k.id=r.sektor ")
                    .append(filterSet)
                    .append(" group by r.id Order By g.name ASC")
            } else {
                builder.append("SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM ")
                    .append(getRoutenTableName())
                    .append(" r")
                    .append(" join ")
                    .append(getAreaTableName())
                    .append(" g on g.id=r.gebiet join ")
                    .append(getSektorenTableName())
                    .append(" k on k.id=r.sektor ")
                    .append(filterSet)
                    .append(" group by r.id Order By r.level DESC")
            }
        } else if (routeType === RouteType.PROJEKT) {
            if (sort === RouteSort.AREA) {
                builder.append("SELECT r.id, r.name,g.name as gebiet,r.level,r.rating, r.kommentar, k.name as sektor FROM ")
                    .append(getProjekteTableName())
                    .append(" r")
                    .append(" join ")
                    .append(getAreaTableName())
                    .append(" g on g.id=r.gebiet join ")
                    .append(getSektorenTableName())
                    .append(" k on k.id=r.sektor ")
                    .append(filterSet)
                    .append(" group by r.id Order By g.name ASC")
            } else {
                builder.append("SELECT r.id, r.name,g.name as gebiet,r.level, r.rating, r.kommentar, k.name as sektor FROM ")
                    .append(getProjekteTableName())
                    .append(" r")
                    .append(" join ")
                    .append(getAreaTableName())
                    .append(" g on g.id=r.gebiet join ")
                    .append(getSektorenTableName())
                    .append(" k on k.id=r.sektor")
                    .append(filterSet)
                    .append(" group by r.id Order By r.level DESC")
            }
        }
        return builder.toString()
    }

    fun getRoute(id: Int): String {
        return "SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM " +
                getRoutenTableName() +
                " r, " +
                getAreaTableName() +
                " g, " +
                getSektorenTableName() +
                " k" +
                " where g.id=r.gebiet and k.id=r.sektor AND r.id=" +
                id
    }

    fun getProjekt(id: Int): String {
        return "SELECT r.id, r.name,g.name as gebiet,r.level,r.rating, r.kommentar, k.name as sektor FROM " +
                getProjekteTableName() +
                " r, " +
                getAreaTableName() +
                " g, " +
                getSektorenTableName() +
                " k" +
                " where g.id=r.gebiet and k.id=r.sektor AND r.id=" +
                id
    }

    fun getTopTenRoutes(year: Int): String {
        return "Select r.level, r.stil, r.name, r.gebiet FROM " +
                getRoutenTableName() +
                " r" +
                " where CAST(strftime('%Y',r.date) as int)==" +
                year +
                " Order By r.level desc, r.stil asc Limit 10"
    }

    fun getYears(filterSet: Boolean): String {
        val filter = AppPreferenceManager.getFilter()
        var sql =
            "select DISTINCT(strftime('%Y',r.date)) as year from " + getRoutenTableName() + " r order by r.date DESC"
        if (filterSet && filter.isNotEmpty()) {
            sql =
                "select DISTINCT(strftime('%Y',r.date)) as year from " + getRoutenTableName() + " r " + filter + " order by r.date DESC"
        }
        return sql
    }

    fun getInsertRouteTasks(route: Route): Array<String> {
        val insertArea =
            "INSERT OR IGNORE INTO " + getAreaTableName() + " (name) VALUES ('" + route.area + "')"
        val insertSektor =
            "INSERT OR IGNORE INTO " + getSektorenTableName() + " (name,gebiet) " +
                    "SELECT '" + route.sector + "',id FROM " + getAreaTableName() + " WHERE name='" + route.area + "'"
        val insertRoute =
            "INSERT OR IGNORE INTO " + getRoutenTableName() + " (date,name,level,stil,rating,kommentar,gebiet,sektor) " +
                    "SELECT '" + route.date + "','" + route.name + "','" + route.level + "','" + route.style + "','" + route.rating + "','" + route.comment + "',a.id,s.id" +
                    " FROM " + getAreaTableName() + " a, " + getSektorenTableName() + " s " +
                    "WHERE a.name = '" + route.area + "'" +
                    "AND s.name='" + route.sector + "'"
        return arrayOf(
            insertArea, insertSektor, insertRoute
        )
    }

    fun getInsertProjektTasks(projekt: Projekt): Array<String> {
        val insertArea =
            "INSERT OR IGNORE INTO " + getAreaTableName() + " (name) VALUES ('" + projekt.area + "')"
        val insertSektor =
            "INSERT OR IGNORE INTO " + getSektorenTableName() + " (name,gebiet) " +
                    "SELECT '" + projekt.sector + "',id FROM " + getAreaTableName() + " WHERE name='" + projekt.area + "'"
        val insertProjekt =
            "INSERT OR IGNORE INTO " + getProjekteTableName() + " (name,level,rating,kommentar,gebiet,sektor) " +
                    "SELECT '" + projekt.name + "','" + projekt.level + "','" + projekt.rating + "','" + projekt.comment + "',a.id,s.id " +
                    "FROM " + getAreaTableName() + " a, " + getSektorenTableName() + " s " +
                    "WHERE a.name = '" + projekt.area + "'" +
                    " AND s.name='" + projekt.sector + "'"
        return arrayOf(insertArea, insertSektor, insertProjekt)
    }

    fun updateRoute(route: Route): String {
        val areaName = route.area
        val sectorName = route.sector
        if (SqlRouten.checkIfNumeric(areaName) && SqlRouten.checkIfNumeric(sectorName)) {
            route.area =
                AreaRepository.getAreaByAreaNameAndSectorName(sectorName, areaName).id.toString()
            route.sector = SectorRepository.getSectorByAreaNameAndSectorName(
                sectorName,
                areaName
            ).id.toString()
        }
        return String.format(
            "UPDATE %s SET date='%s', name='%s', level='%s', stil='%s', rating='%s', kommentar='%s',  gebiet='%s', sektor='%s' where id=%s",
            getRoutenTableName(), route.date, route.name, route.level, route.style, route.rating,
            route.comment, route.area, route.sector, route.id
        )
    }


    fun deleteRoute(id: Int): String {
        return "DELETE FROM " + getRoutenTableName() + " WHERE id=" + id
    }

    fun deleteProjekt(id: Int): String {
        return "DELETE FROM " + getProjekteTableName() + " WHERE id=" + id
    }
}