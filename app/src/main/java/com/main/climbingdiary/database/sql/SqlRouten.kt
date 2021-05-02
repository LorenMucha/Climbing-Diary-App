package com.main.climbingdiary.database.sql

import com.main.climbingdiary.common.AppPreferenceManager
import com.main.climbingdiary.database.entities.AreaRepository
import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.SectorRepository
import com.main.climbingdiary.database.sql.SqlAreaSektoren.getAreaTableName
import com.main.climbingdiary.database.sql.SqlAreaSektoren.getSektorenTableName
import com.main.climbingdiary.model.RouteSort
import com.main.climbingdiary.model.RouteType
import java.util.regex.Pattern

object SqlRouten {
    fun getProjekteTableName(): String {
        return "projekte_${AppPreferenceManager.getSportType()}"
    }

    fun getRoutenTableName(): String {
        return "routen_${AppPreferenceManager.getSportType()}"
    }

    fun getRouteList(routeType: RouteType): String {
        val builder = StringBuilder()
        var filterSet: String = AppPreferenceManager.getFilter()
        filterSet = if (filterSet.isEmpty()) "" else String.format(" where %s", filterSet)
        val sort: RouteSort = AppPreferenceManager.getSort()
        if (routeType === RouteType.ROUTE) {
            if (sort === RouteSort.DATE) {
                """"SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, 
                    r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor 
                    FROM ${getRoutenTableName()} r
                    join ${getAreaTableName()} g on g.id=r.gebiet 
                    join ${getSektorenTableName()} k on k.id=r.sektor $filterSet 
                    group by r.id Order By r.date DESC
                """.trimIndent()
            } else if (sort === RouteSort.AREA) {
                """"SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, 
                    strftime('%d.%m.%Y',r.date) as date, k.name as sektor 
                    FROM ${getRoutenTableName()} r
                    join ${getAreaTableName()} g on g.id=r.gebiet
                    join ${getSektorenTableName()} k on k.id=r.sektor
                    $filterSet
                    group by r.id Order By g.name ASC
                """.trimIndent()
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

    fun getRoute(id: Int): String? {
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

    fun getProjekt(id: Int): String? {
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

    fun getTopTenRoutes(year: Int): String? {
        return "Select r.level, r.stil, r.name, r.gebiet FROM " +
                getRoutenTableName() +
                " r" +
                " where CAST(strftime('%Y',r.date) as int)==" +
                year +
                " Order By r.level desc, r.stil asc Limit 10"
    }

    fun getYears(filterSet: Boolean): String? {
        val filter: String? = AppPreferenceManager.getFilter()
        var sql =
            "select DISTINCT(strftime('%Y',r.date)) as year from " + getRoutenTableName() + " r order by r.date DESC"
        if (filterSet && filter.isNotEmpty()) {
            sql =
                "select DISTINCT(strftime('%Y',r.date)) as year from " + getRoutenTableName() + " r " + filter + " order by r.date DESC"
        }
        return sql
    }

    fun getInsertRouteTasks(route: Route): Array<String>? {
        val insertArea =
            "INSERT OR IGNORE INTO " + getAreaTableName().toString() + " (name) VALUES ('" + route.area + "')"
        val insertSektor =
            "INSERT OR IGNORE INTO " + getSektorenTableName().toString() + " (name,gebiet) " +
                    "SELECT '" + route.sector + "',id FROM " + getAreaTableName().toString() + " WHERE name='" + route.area + "'"
        val insertRoute =
            "INSERT OR IGNORE INTO " + getRoutenTableName() + " (date,name,level,stil,rating,kommentar,gebiet,sektor) " +
                    "SELECT '" + route.date + "','" + route.name + "','" + route.getLevel() + "','" + route.style + "','" + route.rating + "','" + route.comment + "',a.id,s.id" +
                    " FROM " + getAreaTableName() + " a, " + getSektorenTableName() + " s " +
                    "WHERE a.name = '" + route.area + "'" +
                    "AND s.name='" + route.sector + "'"
        return arrayOf(
            insertArea, insertSektor, insertRoute
        )
    }

    fun getInsertProjektTasks(projekt: Projekt): Array<String>? {
        val insertArea =
            "INSERT OR IGNORE INTO " + getAreaTableName().toString() + " (name) VALUES ('" + projekt.area + "')"
        val insertSektor =
            "INSERT OR IGNORE INTO " + getSektorenTableName().toString() + " (name,gebiet) " +
                    "SELECT '" + projekt.sector + "',id FROM " + getAreaTableName().toString() + " WHERE name='" + projekt.area + "'"
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
        if (checkIfNumeric(areaName) && checkIfNumeric(sectorName)) {
            route.setArea(
                Integer.toString(
                    AreaRepository.getInstance()
                        .getAreaByAreaNameAndSectorName(sectorName, areaName).getId()
                )
            )
            route.setSector(
                Integer.toString(
                    SectorRepository.getInstance()
                        .getSectorByAreaNameAndSectorName(sectorName, areaName).getId()
                )
            )
        }
        return java.lang.String.format(
            "UPDATE %s SET date='%s', name='%s', level='%s', stil='%s', rating='%s', kommentar='%s' where id=%s",
            getRoutenTableName(),
            route.date,
            route.name,
            route.getLevel(),
            route.style,
            route.rating,
            route.comment,
            route.id
        )
    }

    fun deleteRoute(id: Int): String? {
        return "DELETE FROM " + getRoutenTableName() + " WHERE id=" + id
    }

    fun deleteProjekt(id: Int): String? {
        return "DELETE FROM " + getProjekteTableName() + " WHERE id=" + id
    }

    private fun checkIfNumeric(toCheck: String?): Boolean {
        val pattern = Pattern.compile("-?\\d+(\\.\\d+)?")
        return if (toCheck == null) {
            true
        } else !pattern.matcher(toCheck).matches()
    }
}